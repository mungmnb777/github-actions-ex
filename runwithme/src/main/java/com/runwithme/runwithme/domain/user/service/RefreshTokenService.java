package com.runwithme.runwithme.domain.user.service;

import com.runwithme.runwithme.domain.user.dto.RefreshTokenDto;
import com.runwithme.runwithme.domain.user.dto.RefreshTokenIssueDto;
import com.runwithme.runwithme.domain.user.entity.RefreshToken;
import com.runwithme.runwithme.domain.user.entity.User;
import com.runwithme.runwithme.domain.user.repository.RefreshTokenRepository;
import com.runwithme.runwithme.domain.user.repository.UserRepository;
import com.runwithme.runwithme.global.error.CustomException;
import com.runwithme.runwithme.global.result.ResultCode;
import com.runwithme.runwithme.global.security.jwt.AuthToken;
import com.runwithme.runwithme.global.security.jwt.AuthTokenFactory;
import com.runwithme.runwithme.global.security.properties.JwtProperties;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final AuthTokenFactory tokenFactory;
    private final JwtProperties properties;

    public void save(RefreshTokenDto dto) {
        RefreshToken entity = RefreshToken.builder()
                .name(dto.tokenName())
                .userEmail(dto.userEmail())
                .expiredDateTime(dto.expiredDatetime())
                .build();

        refreshTokenRepository.save(entity);
    }

    public AuthToken reIssue(RefreshTokenIssueDto dto) {
        RefreshToken token = refreshTokenRepository.findById(dto.refreshToken()).orElseThrow(() -> new CustomException(ResultCode.UNSUPPORTED_JWT_TOKEN));
        AuthToken authToken = tokenFactory.convertAuthToken(token.getName());
        if (authToken.validate()) {
            User user = userRepository.findByEmail(token.getUserEmail()).orElseThrow(() -> new CustomException(ResultCode.USER_NOT_FOUND));
            return tokenFactory.createAuthToken(user.getEmail(), user.getRole().toString(), new Date(System.currentTimeMillis() + properties.accessTokenExpiry));
        } else {
            throw new CustomException(ResultCode.UNSUPPORTED_JWT_TOKEN);
        }
    }

    private boolean isExpired(LocalDateTime expiredDatetime) {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(expiredDatetime);
    }
}
