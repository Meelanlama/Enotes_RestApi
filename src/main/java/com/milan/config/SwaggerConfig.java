package com.milan.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.*;
import io.swagger.v3.oas.models.security.*;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        OpenAPI openApi = new OpenAPI();

        // API basic information
        Info info = new Info();
        info.setTitle("Enotes API");
        info.setDescription("Enotes Api");
        info.setVersion("1.0.0");
        info.setTermsOfService("http://milanservice.com");
        info.setContact(new Contact().email("lamameelan32@gmail.com").name("Milan Tamang").url("http://milan.com/contact"));
        info.setLicense(new License().name("Enotes 1.0").url("http://milan.com"));

        // Define servers for different environments
        List<Server> serverList = List.of(
                new Server().description("Dev").url("http://localhost:8080"),
                new Server().description("Test").url("http://localhost:8081"),
                new Server().description("Prod").url("http://localhost:8083")
        );

        // Define JWT security scheme, token in: bearer aksjaksjaksjka
        SecurityScheme securityScheme = new SecurityScheme()
                .name("Authorization")
                .scheme("bearer")
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER);

        // Add security scheme to components
        Components component = new Components().addSecuritySchemes("Token", securityScheme);

        // Set info, servers, components, and security to OpenAPI object
        openApi.setServers(serverList);
        openApi.setInfo(info);
        openApi.setComponents(component);
        openApi.setSecurity(List.of(new SecurityRequirement().addList("Token")));

        return openApi; // Return configured OpenAPI instance
    }
}
