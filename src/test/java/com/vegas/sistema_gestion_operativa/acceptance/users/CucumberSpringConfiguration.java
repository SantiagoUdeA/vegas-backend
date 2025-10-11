package com.vegas.sistema_gestion_operativa.acceptance.users;

import com.vegas.sistema_gestion_operativa.SecurityTestConfig;
import com.vegas.sistema_gestion_operativa.acceptance.config.FakeJwtFactory;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CucumberSpringConfiguration {

    @Autowired
    protected TestRestTemplate testRestTemplate;

    @Autowired
    protected FakeJwtFactory fakeJwtFactory;

    private static String currentToken = "fake-jwt-token";

    // Delegar al SecurityTestConfig
    public static void setJwt(Supplier<Jwt> supplier) {
        SecurityTestConfig.setJwt(supplier);
        // Configurar el interceptor para agregar el header de Authorization
        currentToken = supplier.get().getTokenValue();
    }

    @TestConfiguration
    static class TestConfig {
        @Autowired
        public void configureRestTemplate(TestRestTemplate testRestTemplate) {
            List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
            interceptors.add((request, body, execution) -> {
                request.getHeaders().add("Authorization", "Bearer " + currentToken);
                return execution.execute(request, body);
            });
            testRestTemplate.getRestTemplate().setInterceptors(interceptors);
        }
    }

}
