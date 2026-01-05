package com.assessment.projectmanagement.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Swagger/OpenAPI Configuration
 * Provides API documentation accessible at /swagger-ui.html
 */
@Configuration
public class SwaggerConfig {

    @Value("${spring.application.name:Project Management API}")
    private String applicationName;

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Project Management System API")
                        .version("1.0.0")
                        .description("""
                                RESTful API for Project and Task Management System

                                ## Features
                                - User authentication with JWT
                                - Project management (CRUD operations)
                                - Task management within projects
                                - Role-based access control
                                - Audit logging

                                ## Authentication
                                Most endpoints require JWT authentication. To use protected endpoints:
                                1. Register a new user via POST /api/auth/register
                                2. Login via POST /api/auth/login to get a JWT token
                                3. Click the 'Authorize' button and enter: Bearer {your-token}
                                4. Now you can access protected endpoints

                                ## Business Rules
                                - Projects can only be activated if they have at least one task
                                - Only project owners can modify their projects and tasks
                                - Completed tasks cannot be modified
                                - All deletions are soft deletes
                                """)
                        .contact(new Contact()
                                .name("Development Team")
                                .email("dev@projectmanagement.com")
                                .url("https://github.com/your-repo"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local Development Server"),
                        new Server()
                                .url("https://api.projectmanagement.com")
                                .description("Production Server (if deployed)")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enter JWT token obtained from /api/auth/login")));
    }
}
