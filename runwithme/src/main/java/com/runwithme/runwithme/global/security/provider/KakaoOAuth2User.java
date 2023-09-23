package com.runwithme.runwithme.global.security.provider;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class KakaoOAuth2User implements OAuth2User {

    private final Map<String, Object> attributes;

    public KakaoOAuth2User(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getName() {
        return (String) attributes.get("email");
    }
}
