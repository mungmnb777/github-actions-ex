package com.runwithme.runwithme.global.security.provider;

import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class ProviderUserFactory {
    public static OAuth2User build(ProviderType providerType, Map<String, Object> attributes) {
        return switch (providerType) {
            case NAVER -> new NaverOAuth2User((Map<String, Object>) attributes.get("response"));
            case KAKAO -> new KakaoOAuth2User((Map<String, Object>) attributes.get("kakao_account"));
        };
    }
}
