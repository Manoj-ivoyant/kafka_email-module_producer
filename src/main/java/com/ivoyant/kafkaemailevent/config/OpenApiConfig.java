package com.ivoyant.kafkaemailevent.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    OpenAPI customOpenApi() {
        return new OpenAPI().info(new Info().title("Email sender application")
                .summary("This application is used to send a email which creates email event"));

    }
}
