package com.runwithme.runwithme.global.error;

import com.runwithme.runwithme.global.result.ResultCode;
import com.runwithme.runwithme.global.result.ResultResponseDto;
import com.runwithme.runwithme.global.webhook.NotificationManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Enumeration;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final NotificationManager notificationManager;

    @ExceptionHandler({CommonException.class})
    protected ResponseEntity<ResultResponseDto> exceptionHandler(CommonException e, HttpServletRequest request) {
        notificationManager.sendNotification(e, request.getRequestURI(), getParams(request));
        ResultCode resultCode = e.getResultCode();
        return ResponseEntity
                .status(resultCode.getStatus())
                .body(ResultResponseDto.of(resultCode, resultCode.getMessage()));
    }

    private String getParams(HttpServletRequest req) {
        StringBuilder params = new StringBuilder();
        Enumeration<String> keys = req.getParameterNames();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            params.append(
                    """
                            - %s : %s
                            """.formatted(key, req.getParameter(key))
            );
        }
        return params.toString();
    }
}
