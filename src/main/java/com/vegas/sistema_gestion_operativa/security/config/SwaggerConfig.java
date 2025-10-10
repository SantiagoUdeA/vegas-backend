package com.vegas.sistema_gestion_operativa.security.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"dev"})
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API - Sistema de gestión operativa integral - Grupo las vegas")
                        .description("Documentación de la API REST para el sistema el sistema de gestion operativa.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Repositorio GitHub")
                                .url("https://github.com/SantiagoUdeA/BancoUdeA-backend")));
    }
}
