package com.vegas.sistema_gestion_operativa.security.configuration;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@Profile({"dev", "prod"})
@Slf4j
public class SecurityConfig {

    /**
     * Configures the security filter chain.
     * Permits access to Swagger UI and API docs without authentication.
     * Secures other endpoints with JWT-based OAuth2 resource server.
     * @param http HttpSecurity to configure
     * @return configured SecurityFilterChain
     * @throws Exception in case of configuration errors
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("Security filter chain configuration initialized.");
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));
        return http.build();
    }

    /**
     * Configures the JWT authentication converter to extract roles from custom claims.
     * @return configured JwtAuthenticationConverter
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(this::extractAuthorities);
        return converter;
    }

    /**
     * Configures the method security expression handler with a custom permission evaluator.
     * @param permissionEvaluator custom permission evaluator
     * @return configured MethodSecurityExpressionHandler
     */
    @Bean
    static MethodSecurityExpressionHandler methodSecurityExpressionHandler(CustomPermissionEvaluator permissionEvaluator) {
        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
        handler.setPermissionEvaluator(permissionEvaluator);
        return handler;
    }

    /**
     * Extracts authorities from JWT claims.
     * Maps the "custom:role" claim to a Spring Security authority.
     * @param jwt JWT token
     * @return collection of granted authorities
     */
    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        Optional<String> role = jwt.getClaimAsString("role").describeConstable();
        if (role.isEmpty()) return List.of();
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.get()));
    }
}