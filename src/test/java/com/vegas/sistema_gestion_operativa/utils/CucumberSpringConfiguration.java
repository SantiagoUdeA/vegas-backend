package com.vegas.sistema_gestion_operativa.utils;

import com.vegas.sistema_gestion_operativa.security.configuration.CustomPermissionEvaluator;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.function.Supplier;

/**
 * Configuración base de Spring para las pruebas de aceptación con Cucumber.
 * Configura el contexto de Spring Boot, Spring Security y el TestRestTemplate con autenticación JWT.
 */
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CucumberSpringConfiguration {

    @Autowired
    protected TestRestTemplate testRestTemplate;

    @Autowired
    protected FakeJwtFactory fakeJwtFactory;

    private static JwtAuthorizationInterceptor jwtInterceptor;
    private static Supplier<Jwt> jwtSupplier;

    /**
     * Configura el JWT para las pruebas de autenticación.
     * @param supplier proveedor del JWT con el rol configurado
     */
    public static void setJwt(Supplier<Jwt> supplier) {
        jwtSupplier = supplier;
        if (jwtInterceptor != null) {
            jwtInterceptor.setCurrentToken(supplier.get().getTokenValue());
        }
    }

    /**
     * Configuración de seguridad y componentes de prueba.
     * Centraliza todos los beans necesarios para las pruebas de aceptación.
     */
    @TestConfiguration
    @EnableWebSecurity
    @EnableMethodSecurity
    static class TestConfig {

        /**
         * Configura el TestRestTemplate para agregar automáticamente
         * el header de Authorization en todas las peticiones HTTP.
         */
        @Autowired
        public void configureRestTemplate(TestRestTemplate testRestTemplate) {
            jwtInterceptor = new JwtAuthorizationInterceptor("fake-jwt-token");
            testRestTemplate.getRestTemplate().setInterceptors(Collections.singletonList(jwtInterceptor));
        }

        /**
         * Configura la cadena de filtros de seguridad para los tests.
         * Desactiva CSRF y configura OAuth2 Resource Server con JWT.
         */
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                    .oauth2ResourceServer(oauth2 -> oauth2
                            .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                    );
            return http.build();
        }

        /**
         * Convierte los claims del JWT en authorities de Spring Security.
         * Extrae el claim "custom:role" y lo convierte en "ROLE_[ROLE_NAME]".
         */
        @Bean
        public JwtAuthenticationConverter jwtAuthenticationConverter() {
            JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
            converter.setJwtGrantedAuthoritiesConverter(jwt -> {
                String role = jwt.getClaimAsString("custom:role");
                if (role != null) {
                    return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
                }
                return Collections.emptyList();
            });
            return converter;
        }

        /**
         * JwtDecoder mock que retorna el JWT configurado dinámicamente.
         */
        @Bean
        public JwtDecoder jwtDecoder() {
            return token -> {
                if (jwtSupplier == null) {
                    throw new IllegalStateException("JWT supplier not configured. Call setJwt() first.");
                }
                return jwtSupplier.get();
            };
        }

        /**
         * Configura el handler de expresiones de seguridad con el evaluador de permisos personalizado.
         */
        @Bean
        static MethodSecurityExpressionHandler methodSecurityExpressionHandler(CustomPermissionEvaluator permissionEvaluator) {
            DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
            handler.setPermissionEvaluator(permissionEvaluator);
            return handler;
        }
    }
}
