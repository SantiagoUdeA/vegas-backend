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
            "Admin", Role.ADMIN,
            "ADMIN", Role.ADMIN,
            "Cashier", Role.CASHIER,
            "CASHIER", Role.CASHIER,
            "Owner", Role.OWNER,
            "OWNER", Role.OWNER
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
                .filter(r -> !r.isBlank()) // 👈 Filtra authorities vacías
                .orElse(null);

        if (roleName == null) {
            System.err.println("⚠️  No valid role found in authentication"); // Debug
            return false;
        }

        Role role = ROLE_MAPPING.get(roleName);
        if (role == null) {
            System.err.println("⚠️  Role not mapped: " + roleName); // Debug
            return false;
        }

        try {
            Permission required = Permission.valueOf(permission.toString());
            boolean hasPermission = role.hasPermission(required);
            System.out.printf("✓ Role '%s' %s permission '%s'%n",
                    roleName, hasPermission ? "HAS" : "LACKS", required); // Debug
            return hasPermission;
        } catch (IllegalArgumentException e) {
            System.err.println("⚠️  Invalid permission: " + permission); // Debug
            return false;
        }
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return hasPermission(authentication, targetId, permission);
    }
}