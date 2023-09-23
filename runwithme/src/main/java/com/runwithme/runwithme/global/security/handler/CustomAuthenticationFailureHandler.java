package com.runwithme.runwithme.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.runwithme.runwithme.global.error.CustomException;
import com.runwithme.runwithme.global.result.ResultCode;
import com.runwithme.runwithme.global.result.ResultResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.CharEncoding;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.PrintWriter;

import static com.runwithme.runwithme.global.result.ResultCode.SEQ_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        response.setCharacterEncoding(CharEncoding.UTF_8);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        setResponseBody(response);
    }

    private void setResponseBody(HttpServletResponse response) {
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String responseBody;
        try {
            responseBody = om.writerWithDefaultPrettyPrinter().writeValueAsString(ResultResponseDto.of(ResultCode.UNAUTHORIZED));
            PrintWriter writer = response.getWriter();
            writer.write(responseBody);
        } catch (Exception e) {
            throw new CustomException(SEQ_NOT_FOUND);
        }
    }
}
