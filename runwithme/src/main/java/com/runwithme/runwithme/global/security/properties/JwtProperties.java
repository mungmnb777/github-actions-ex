package com.runwithme.runwithme.global.security.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("jwt")
public class JwtProperties {

    public String secret;

    public int accessTokenExpiry;

    public int refreshTokenExpiry;
}
