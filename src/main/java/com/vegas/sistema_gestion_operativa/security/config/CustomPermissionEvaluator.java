package com.vegas.sistema_gestion_operativa.security.config;

import com.vegas.sistema_gestion_operativa.roles.domain.Permission;
import com.vegas.sistema_gestion_operativa.roles.domain.Role;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    // Mapeo de nombres de Cognito a roles del sistema
    private static final Map<String, Role> ROLE_MAPPING = Map.of(
            "Administrador", Role.ADMIN,
            "ADMIN", Role.ADMIN,
            "Cajero", Role.CASHIER,
            "CAJERO", Role.CASHIER,
            "Dueno", Role.OWNER,
            "DUENO", Role.OWNER
    );

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication == null || permission == null) {
            return false;
        }

        String roleName = authentication.getAuthorities()
                .stream()
                .findFirst()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .orElse(null);

        if (roleName == null) {
            return false;
        }

        Role role = ROLE_MAPPING.get(roleName);
        if (role == null) {
            return false;
        }

        try {
            Permission required = Permission.valueOf(permission.toString());
            return role.hasPermission(required);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return hasPermission(authentication, targetId, permission);
    }
}