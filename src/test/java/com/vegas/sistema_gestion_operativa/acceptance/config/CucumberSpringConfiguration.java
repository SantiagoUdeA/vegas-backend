package com.vegas.sistema_gestion_operativa.acceptance.config;

import com.vegas.sistema_gestion_operativa.security.config.CustomPermissionEvaluator;
import com.vegas.sistema_gestion_operativa.users.service.UserService;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.mock;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class CucumberSpringConfiguration {

    @TestConfiguration
    static class TestConfig {

        @Bean
        public JwtDecoder jwtDecoder() {
            return mock(JwtDecoder.class);
        }

        @Bean
        @Primary
        public UserService userService() {
            return mock(UserService.class);
        }

        @Bean
        @Primary
        public CustomPermissionEvaluator customPermissionEvaluator() {
            return mock(CustomPermissionEvaluator.class);
        }
    }
}
