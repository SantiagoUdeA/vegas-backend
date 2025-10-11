package com.vegas.sistema_gestion_operativa.acceptance.config;

import com.vegas.sistema_gestion_operativa.roles.domain.Role;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class FakeJwtFactory {

    public Jwt createJwtWithRole(Role role) {
        return Jwt.withTokenValue("fake-jwt-token")
                .header("alg", "none")
                .claim("custom:role", role.name())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();
    }

}
