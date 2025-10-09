package com.vegas.sistema_gestion_operativa.security.service;

import com.vegas.sistema_gestion_operativa.roles.domain.Permission;
import com.vegas.sistema_gestion_operativa.roles.domain.Role;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {

    public boolean hasPermission(Authentication auth, Permission permission) {
        if (auth == null || auth.getAuthorities().isEmpty()) return false;

        // Extrae el rol (ej: "ROLE_ADMIN")
        String roleName = auth.getAuthorities()
                .iterator()
                .next()
                .getAuthority()
                .replace("ROLE_", "");

        try {
            Role role = Role.valueOf(roleName);
            return role.hasPermission(permission);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public void checkPermission(Authentication auth, Permission permission) {
        if (!hasPermission(auth, permission)) {
            throw new AccessDeniedException("No tienes permiso para esta acción");
        }
    }
}
