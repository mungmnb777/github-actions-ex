package com.runwithme.runwithme.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.runwithme.runwithme.domain.user.dto.UserLoginDto;
import com.runwithme.runwithme.global.error.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.runwithme.runwithme.global.result.ResultCode.HEADER_NO_TOKEN;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(request);
        setDetails(request, authenticationToken);
        Authentication authenticate;
        try {
            authenticate = this.getAuthenticationManager().authenticate(authenticationToken);
        } catch (Exception e) {
            request.setAttribute("exception", e);
            throw e;
        }
        return authenticate;
    }

    private static UsernamePasswordAuthenticationToken getAuthenticationToken(HttpServletRequest request) {
        try {
            UserLoginDto dto = new ObjectMapper().readValue(request.getInputStream(), UserLoginDto.class);
            log.debug("CustomAuthenticationFilter :: email : {}, password : {}", dto.email(), dto.password());
            return new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        } catch (Exception e) {
            throw new CustomException(HEADER_NO_TOKEN);
        }
    }
}
