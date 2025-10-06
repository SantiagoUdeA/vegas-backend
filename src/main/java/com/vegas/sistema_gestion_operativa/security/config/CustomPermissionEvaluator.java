package com.vegas.sistema_gestion_operativa.security.config;

import com.vegas.sistema_gestion_operativa.security.domain.Permission;
import com.vegas.sistema_gestion_operativa.security.domain.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    private static final Logger log = LoggerFactory.getLogger(CustomPermissionEvaluator.class);

    // Mapeo de nombres de Cognito a roles del sistema
    private static final Map<String, Role> ROLE_MAPPING = Map.of(
            "Administrador", Role.ADMIN,
            "ADMIN", Role.ADMIN,
            "Cajero", Role.CAJERO,
            "CAJERO", Role.CAJERO,
            "Gerente", Role.GERENTE,
            "GERENTE", Role.GERENTE,
            "Gerente Produccion", Role.GERENTE_PRODUCCION,
            "GERENTE_PRODUCCION", Role.GERENTE_PRODUCCION,
            "Dueno", Role.DUENO,
            "DUENO", Role.DUENO
    );

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication == null || permission == null) {
            log.warn("Authentication or permission is null - authentication: {}, permission: {}",
                    authentication, permission);
            return false;
        }

        log.debug("Evaluating permission - User: {}, Authorities: {}, Permission: {}",
                authentication.getName(),
                authentication.getAuthorities(),
                permission);

        String cognitoRoleName = authentication.getAuthorities()
                .stream()
                .findFirst()
                .map(a -> {
                    String authority = a.getAuthority().replace("ROLE_", "");
                    log.debug("Cognito role name: {}", authority);
                    return authority;
                })
                .orElse(null);

        if (cognitoRoleName == null) {
            log.warn("No role found for user: {}", authentication.getName());
            return false;
        }

        Role role = ROLE_MAPPING.get(cognitoRoleName);
        if (role == null) {
            log.error("Role '{}' from Cognito is not mapped to any system role", cognitoRoleName);
            return false;
        }

        try {
            Permission required = Permission.valueOf(permission.toString());
            boolean hasPermission = role.hasPermission(required);

            log.debug("Cognito Role: {}, Mapped Role: {}, Required Permission: {}, Has Permission: {}",
                    cognitoRoleName, role, required, hasPermission);

            return hasPermission;
        } catch (IllegalArgumentException e) {
            log.error("Invalid permission: {}", permission, e);
            return false;
        }
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return hasPermission(authentication, (Object) targetId, permission);
    }
}