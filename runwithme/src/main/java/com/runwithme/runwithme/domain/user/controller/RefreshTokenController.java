package com.runwithme.runwithme.domain.user.controller;

import com.runwithme.runwithme.domain.user.dto.RefreshTokenIssueDto;
import com.runwithme.runwithme.domain.user.service.RefreshTokenService;
import com.runwithme.runwithme.global.security.jwt.AuthToken;
import com.runwithme.runwithme.global.utils.HeaderUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;

    @PostMapping
    public ResponseEntity<Void> reIssue(@ModelAttribute RefreshTokenIssueDto dto, HttpServletResponse response) {
        if (StringUtils.equals(dto.grantType(), "refresh_token")) {
            AuthToken accessToken = refreshTokenService.reIssue(dto);
            HeaderUtils.setAccessToken(response, accessToken.getToken());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            throw new IllegalArgumentException();
        }
    }
}
