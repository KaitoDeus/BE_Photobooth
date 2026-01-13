package com.photobooth.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * ============================================
 * OpenAPI Configuration
 * ============================================
 * Configures Swagger/OpenAPI documentation.
 * Access documentation at /api/docs
 * ============================================
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:3000}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Photobooth API")
                        .version("1.0.0")
                        .description("REST API documentation for Photobooth Web Application")
                        .contact(new Contact()
                                .name("Photobooth Team")
                                .email("support@photobooth.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Development Server")));
    }
}
