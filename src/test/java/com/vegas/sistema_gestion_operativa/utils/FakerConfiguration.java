package com.vegas.sistema_gestion_operativa.utils;

import net.datafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FakerConfiguration {

    @Bean
    public Faker faker() {
        return new Faker();
    }
}
