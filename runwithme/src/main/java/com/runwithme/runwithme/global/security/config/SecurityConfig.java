package com.runwithme.runwithme.global.security.config;

import com.runwithme.runwithme.domain.user.service.RefreshTokenService;
import com.runwithme.runwithme.global.error.CustomException;
import com.runwithme.runwithme.global.security.filter.CustomAuthenticationFilter;
import com.runwithme.runwithme.global.security.filter.TokenAuthorizationFilter;
import com.runwithme.runwithme.global.security.handler.CustomAuthenticationFailureHandler;
import com.runwithme.runwithme.global.security.handler.CustomAuthenticationSuccessHandler;
import com.runwithme.runwithme.global.security.handler.OAuth2AuthenticationSuccessHandler;
import com.runwithme.runwithme.global.security.jwt.AuthTokenFactory;
import com.runwithme.runwithme.global.security.point.DelegatedAuthenticationEntryPoint;
import com.runwithme.runwithme.global.security.properties.JwtProperties;
import com.runwithme.runwithme.global.security.provider.CustomAuthenticationProvider;
import com.runwithme.runwithme.global.security.repository.CustomAuthorizationRequestRepository;
import com.runwithme.runwithme.global.security.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    private final String[] PERMIT_URL_PATHS = {
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

    private final AuthTokenFactory authTokenFactory;

    private final JwtProperties properties;

    private final CustomOAuth2UserService customOAuth2UserService;

    private final DelegatedAuthenticationEntryPoint authEntryPoint;

    private final RefreshTokenService refreshTokenService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(authorizationEndpointConfig ->
                                authorizationEndpointConfig.baseUri("/oauth2/authorization")
                                        .authorizationRequestRepository(authorizationRequestRepository()))
                        .userInfoEndpoint(
                                userInfoEndpointConfig ->
                                        userInfoEndpointConfig.userService(customOAuth2UserService)
                        )
                        .successHandler(oAuth2AuthenticationSuccessHandler()))
                .authorizeHttpRequests()
                .requestMatchers(PERMIT_URL_PATHS).permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling(handler -> handler.authenticationEntryPoint(authEntryPoint))
                .addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(tokenAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
        return new CustomAuthorizationRequestRepository();
    }

    @Bean
    public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return new OAuth2AuthenticationSuccessHandler(authTokenFactory, properties);
    }

    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter() throws CustomException {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter();

        customAuthenticationFilter.setFilterProcessesUrl("/users/login");
        customAuthenticationFilter.setAuthenticationManager(authenticationManager());
        customAuthenticationFilter.setAuthenticationSuccessHandler(new CustomAuthenticationSuccessHandler(authTokenFactory, properties, refreshTokenService));
        customAuthenticationFilter.setAuthenticationFailureHandler(new CustomAuthenticationFailureHandler());

        return customAuthenticationFilter;
    }

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(passwordEncoder());
    }

    @Bean
    public TokenAuthorizationFilter tokenAuthorizationFilter() {
        return new TokenAuthorizationFilter(authTokenFactory);
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(customAuthenticationProvider());
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("HEAD", "POST", "GET", "DELETE", "PUT"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
