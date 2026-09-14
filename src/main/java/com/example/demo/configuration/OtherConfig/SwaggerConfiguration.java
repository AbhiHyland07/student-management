package com.example.demo.configuration.OtherConfig;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info =
        @Info(
            title = "Spring Boot API for Student Management",
            version = "3.5.0",
            description = "API documentation for Student Management System",
            contact =
                @Contact(name = "Abhigyan Majumder", email = "abhigyanmajumder007@gmail.com")))
@SecurityScheme(
    name = "Bearer Authentication",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    description =
        "JWT Access Token received from /auth/login endpoint. "
            + "Add this token in the Authorization header as: Bearer <your_token>")
public class SwaggerConfiguration {}
