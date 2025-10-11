package com.vegas.sistema_gestion_operativa.utils;

import com.vegas.sistema_gestion_operativa.roles.domain.Role;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * Factory para crear tokens JWT falsos para pruebas.
 * Los tokens creados incluyen los claims necesarios para la autenticación y autorización.
 */
@Component
public class FakeJwtFactory {

    private static final String TOKEN_VALUE = "fake-jwt-token";
    private static final String HEADER_ALG = "alg";
    private static final String ALG_NONE = "none";
    private static final String CLAIM_ROLE = "custom:role";
    private static final long TOKEN_EXPIRATION_SECONDS = 3600L;

    /**
     * Crea un JWT falso con el rol especificado.
     *
     * @param role el rol que tendrá el usuario autenticado
     * @return JWT configurado con el rol especificado
     */
    public Jwt createJwtWithRole(Role role) {
        return Jwt.withTokenValue(TOKEN_VALUE)
                .header(HEADER_ALG, ALG_NONE)
                .claim(CLAIM_ROLE, role.name())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(TOKEN_EXPIRATION_SECONDS))
                .build();
    }

}
