package com.runwithme.runwithme.global.security.provider;

import com.runwithme.runwithme.domain.user.entity.User;
import com.runwithme.runwithme.domain.user.service.UserService;
import com.runwithme.runwithme.global.error.CustomException;
import com.runwithme.runwithme.global.security.model.PrincipalDetails;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.runwithme.runwithme.global.result.ResultCode.BAD_PASSWORD;
import static com.runwithme.runwithme.global.result.ResultCode.DELETED_USER;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final PasswordEncoder encoder;

    @Resource
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

        String email = (String) token.getPrincipal();
        String password = (String) token.getCredentials();

        User user = userService.findByEmail(email);
        PrincipalDetails principal = new PrincipalDetails(user, null);

        if (!encoder.matches(password, user.getPassword())) {
            throw new CustomException(BAD_PASSWORD);
        }

        if (user.isDeleted()) throw new CustomException(DELETED_USER);

        return new UsernamePasswordAuthenticationToken(principal, password, principal.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
