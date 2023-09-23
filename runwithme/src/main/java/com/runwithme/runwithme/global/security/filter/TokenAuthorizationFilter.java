package com.runwithme.runwithme.global.security.filter;

import com.runwithme.runwithme.global.security.jwt.AuthToken;
import com.runwithme.runwithme.global.security.jwt.AuthTokenFactory;
import com.runwithme.runwithme.global.utils.HeaderUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
public class TokenAuthorizationFilter extends OncePerRequestFilter {
    private final AuthTokenFactory tokenFactory;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String[] PERMIT_URL_PATHS = {
                /* SWAGGER */
                "/favicon.ico",
                "/error",
                "/swagger-ui/**",
                "/swagger-resources/**",
                "/v3/api-docs/**",

                /* ACTUATOR */
                "/management/**",

                /* USER */
                "/users/join",
                "/users/duplicate-email",
                "/users/duplicate-nickname",
                "/users/**/profile-image",
                "/token"
        };
        return Arrays.stream(PERMIT_URL_PATHS)
                .anyMatch(e -> new AntPathMatcher().match(e, request.getServletPath()));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String tokenStr = HeaderUtils.getAccessToken(request);
        AuthToken token = tokenFactory.convertAuthToken(tokenStr);
        if (token.validate()) {
            Authentication authentication = tokenFactory.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }


}
