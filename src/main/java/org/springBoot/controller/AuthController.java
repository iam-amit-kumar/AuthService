package org.springBoot.controller;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springBoot.entities.RefreshToken;
import org.springBoot.model.UserInfoDto;
import org.springBoot.response.JwtResponseDto;
import org.springBoot.service.JwtService;
import org.springBoot.service.RefreshTokenService;
import org.springBoot.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class AuthController {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("auth/v1/signup")
    public ResponseEntity signUp (@RequestBody UserInfoDto userInfoDto) {
        log.debug("Controller received: {}", userInfoDto);

        try {
            Boolean isSignUp = userDetailsService.signUpUser(userInfoDto);

            if(Boolean.FALSE.equals(isSignUp)) {
                return new ResponseEntity<>("Already exists", HttpStatus.BAD_REQUEST);
            }

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userInfoDto.getUsername());
            String jwtToken = jwtService.GenerateToken(userInfoDto.getUsername());

            return new ResponseEntity<>(JwtResponseDto.builder().accessToken(jwtToken)
                    .token(refreshToken.getToken()).build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Exception in User Service", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
