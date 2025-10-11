package com.vegas.sistema_gestion_operativa.utils;

import lombok.Setter;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * HTTP interceptor que agrega automáticamente el header de Authorization
 * con el token JWT a todas las peticiones de TestRestTemplate.
 */
@Setter
public class JwtAuthorizationInterceptor implements ClientHttpRequestInterceptor {

    private String currentToken;

    public JwtAuthorizationInterceptor(String initialToken) {
        this.currentToken = initialToken;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders().add("Authorization", "Bearer " + currentToken);
        return execution.execute(request, body);
    }

}

