package com.runwithme.runwithme.global.security.service;

import com.runwithme.runwithme.domain.user.entity.User;
import com.runwithme.runwithme.domain.user.repository.UserRepository;
import com.runwithme.runwithme.global.security.model.PrincipalDetails;
import com.runwithme.runwithme.global.security.provider.ProviderType;
import com.runwithme.runwithme.global.security.provider.ProviderUserFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    private String parseRegistrationId(OAuth2UserRequest userRequest) {
        return userRequest.getClientRegistration().getRegistrationId().toUpperCase();
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();

        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        return this.process(userRequest, oAuth2User);
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        ProviderType providerType = ProviderType.valueOf(parseRegistrationId(userRequest));

        OAuth2User loginUser = ProviderUserFactory.build(providerType, oAuth2User.getAttributes());

        Optional<User> checkUser = userRepository.findByEmail(loginUser.getName());

        User user = checkUser.orElseGet(() -> create(loginUser));

        return new PrincipalDetails(user, loginUser.getAttributes());
    }

    private User create(OAuth2User user) {
        return userRepository.save(User.create(user));
    }
}
