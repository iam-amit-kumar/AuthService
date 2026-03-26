package org.springBoot.service;

import org.springBoot.entities.RefreshToken;
import org.springBoot.entities.UserInfo;
import org.springBoot.repository.RefreshTokenRepository;
import org.springBoot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserRepository userRepository;

    public RefreshToken createRefreshToken(String userName) {
        UserInfo userInfoExtracted = userRepository.findByUsername(userName);
        RefreshToken refreshToken = RefreshToken.builder()
                .userInfo(userInfoExtracted)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(600000))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token was expired. Please make a new login...!");
        }
        return token;
    }

    public Optional<RefreshToken> findByToken (String token) {
        return refreshTokenRepository.findByToken(token);
    }
}
