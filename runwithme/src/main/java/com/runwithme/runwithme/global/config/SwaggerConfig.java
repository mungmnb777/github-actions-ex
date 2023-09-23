package com.runwithme.runwithme.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("user-api")
                .pathsToMatch("/users/**")
                .build();
    }

    @Bean
    public GroupedOpenApi challengeApi() {
        return GroupedOpenApi.builder()
                .group("challenge-api")
                .pathsToMatch("/challenge/**")
                .build();
    }

    @Bean
    public OpenAPI apiInfo() {
        String jwt = "JWT";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
        Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
                .name(jwt)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
        );

        return new OpenAPI()
                .info(new Info().title("Swagger Documents")
                .description("스웨거 API 문서입니다.")
                .version("v0.1"))
                .addSecurityItem(securityRequirement)
                .components(components);
    }
}
