package com.runwithme.runwithme.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.runwithme.runwithme.domain.user.dto.RefreshTokenDto;
import com.runwithme.runwithme.domain.user.dto.converter.UserConverter;
import com.runwithme.runwithme.domain.user.entity.User;
import com.runwithme.runwithme.domain.user.service.RefreshTokenService;
import com.runwithme.runwithme.global.result.ResultCode;
import com.runwithme.runwithme.global.result.ResultResponseDto;
import com.runwithme.runwithme.global.security.jwt.AuthToken;
import com.runwithme.runwithme.global.security.jwt.AuthTokenFactory;
import com.runwithme.runwithme.global.security.model.PrincipalDetails;
import com.runwithme.runwithme.global.security.properties.JwtProperties;
import com.runwithme.runwithme.global.utils.CookieUtils;
import com.runwithme.runwithme.global.utils.HeaderUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.CharEncoding;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Date;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REFRESH_TOKEN;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final AuthTokenFactory tokenFactory;
    private final JwtProperties properties;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        PrincipalDetails details = (PrincipalDetails) authentication.getPrincipal();
        User user = details.getUser();

        AuthToken accessToken = tokenFactory.createAuthToken(user.getEmail(), user.getRole().toString(), new Date(System.currentTimeMillis() + properties.accessTokenExpiry));
        log.debug("Successful Authentication :: AccessToken : {}", accessToken.getToken());
        HeaderUtils.setAccessToken(response, accessToken.getToken());
        response.setCharacterEncoding(CharEncoding.UTF_8);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        setResponseBody(user, response);

        AuthToken refreshToken = tokenFactory.createAuthToken(null, new Date(System.currentTimeMillis() + properties.refreshTokenExpiry));
        refreshTokenService.save(new RefreshTokenDto(refreshToken.getToken(), user.getEmail(), LocalDateTime.now().plusSeconds(properties.refreshTokenExpiry / 1000)));
        log.debug("Successful Authentication :: RefreshToken : {}", refreshToken.getToken());
        CookieUtils.deleteCookie(request, response, REFRESH_TOKEN);
        CookieUtils.addCookie(response, REFRESH_TOKEN, refreshToken.getToken(), properties.refreshTokenExpiry);
    }

    private void setResponseBody(User loginUser, HttpServletResponse response) throws IOException {
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String responseBody = om.writerWithDefaultPrettyPrinter().writeValueAsString(ResultResponseDto.of(ResultCode.LOGIN_SUCCESS, UserConverter.toViewDto(loginUser)));
        PrintWriter writer = response.getWriter();
        writer.write(responseBody);
    }
}
