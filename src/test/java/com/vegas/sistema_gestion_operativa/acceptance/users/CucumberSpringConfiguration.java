package com.vegas.sistema_gestion_operativa.acceptance.config;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.util.function.Supplier;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CucumberSpringConfiguration {

    @Autowired
    protected TestRestTemplate testRestTemplate;

    @Autowired
    protected FakeJwtFactory fakeJwtFactory;

    private static Supplier<Jwt> jwtSupplier;

    @Bean
    public JwtDecoder jwtDecoder() {
        return token -> jwtSupplier.get();
    }

    public static void setJwt(Supplier<Jwt> supplier) {
        jwtSupplier = supplier;
    }

    @TestConfiguration
    static class TestConfig {




    }


}
