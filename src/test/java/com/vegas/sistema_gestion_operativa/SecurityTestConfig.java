package com.vegas.sistema_gestion_operativa;

import com.vegas.sistema_gestion_operativa.security.config.CustomPermissionEvaluator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@ActiveProfiles("test")
public class SecurityTestConfig {

    private static Supplier<Jwt> jwtSupplier;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Deshabilitar CSRF para tests
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))); // Habilitar JWT con converter
        return http.build();
    }

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

    @Bean
    public JwtDecoder jwtDecoder() {
        return token -> {
            if (jwtSupplier == null) {
                throw new IllegalStateException("JWT supplier not configured");
            }
            return jwtSupplier.get();
        };
    }

    public static void setJwt(Supplier<Jwt> supplier) {
        jwtSupplier = supplier;
    }

    @Bean
    static MethodSecurityExpressionHandler methodSecurityExpressionHandler(CustomPermissionEvaluator permissionEvaluator) {
        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
        handler.setPermissionEvaluator(permissionEvaluator);
        return handler;
    }
}
