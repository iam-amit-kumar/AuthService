package org.springBoot.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springBoot.entities.UserInfo;
import org.springBoot.eventProducer.UserInfoProducer;
import org.springBoot.model.UserInfoDto;
import org.springBoot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

@Component
@AllArgsConstructor
@Data
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final UserInfoProducer userInfoProducer;

    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.debug("Entering in loadUserByUsername Method...");
        UserInfo user = userRepository.findByUsername(username);

        if(user == null){
            log.error("User not found: " + username);
            throw new UsernameNotFoundException("Could not found user with username: " + username);
        }
        log.info("User Authenticated sucessfully...!");
        return new CustomUserDetails(user);
    }

    public UserInfo checkIfUserAlreadyExists(UserInfoDto userInfoDto){
        return userRepository.findByUsername(userInfoDto.getUsername());
    }

    public Boolean signUpUser(UserInfoDto userInfoDto) {
        //Define a function to check if userEmail, password is correct
        //ValidationUtils.validateUserInfo(userInfoDto);

        log.debug("Received userInfoDto: {}", userInfoDto);

        userInfoDto.setPassword(passwordEncoder.encode(userInfoDto.getPassword()));

        if(Objects.nonNull(checkIfUserAlreadyExists(userInfoDto))) {
            return false;
        }

        String userId = UUID.randomUUID().toString();
        UserInfo userInfo = new UserInfo(userId, userInfoDto.getUsername(), userInfoDto.getPassword(), new HashSet<>());
        userRepository.save(userInfo);
        //Push event to Queue/kafka
        userInfoProducer.sendEventToKafka(userInfoDto);
        return true;
    }
}
