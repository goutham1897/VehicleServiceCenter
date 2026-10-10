package com.example.vehicleservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI vehicleServiceOpenAPI() {

        return new OpenAPI()
                .info(
                    new Info()
                        .title("Vehicle Service Center API")
                        .description(
                            "REST API for managing customers, " +
                            "vehicles, service requests, " +
                            "mechanics, parts and invoices."
                        )
                        .version("1.0")
                        .contact(
                            new Contact()
                                .name("Vehicle Service Center")
                        )
                )
                .components(
                    new Components()
                        .addSecuritySchemes(
                            "bearerAuth",
                            new SecurityScheme()
                                .name("Authorization")
                                .type(
                                    SecurityScheme.Type.HTTP
                                )
                                .scheme("bearer")
                                .bearerFormat("JWT")
                        )
                );
    }
}