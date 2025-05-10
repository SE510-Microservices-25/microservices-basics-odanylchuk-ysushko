package com.example.demo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        String keycloakAuthority = "http://microservices.local:8080/realms/MyRealm";
        return new OpenAPI()
                .info(new Info().title("Keycloak-protected-API").version("v1"))
                .addSecurityItem(new SecurityRequirement().addList("Keycloak"))
                .components(new Components()
                        .addSecuritySchemes("Keycloak", new SecurityScheme()
                                .type(SecurityScheme.Type.OAUTH2)
                                .flows(new OAuthFlows()
                                    .implicit(new OAuthFlow()
                                        .authorizationUrl(keycloakAuthority + "/protocol/openid-connect/auth")
                                        .tokenUrl(keycloakAuthority + "/protocol/openid-connect/token")
                                        .scopes(new Scopes()
                                            .addString("openid", "OpenID Connect scope")
                                            .addString("profile", "User profile")
                                            .addString("email", "User email")
                                        )
                                    )
                                )
                        )
                );
    }
}