package com.vegas.sistema_gestion_operativa.roles.application.service;

import com.vegas.sistema_gestion_operativa.roles.domain.Permission;
import com.vegas.sistema_gestion_operativa.roles.domain.Role;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {

    public boolean hasPermission(Role role, Permission permission) {
        return role.getPermissions().contains(permission);
    }

    public boolean canCreateUserWithRole(Role userRole, Role newUserRole) {
        if (newUserRole.equals(Role.ROOT)) {
            return false; // Rol inválido
        }
        if (userRole.equals(Role.OWNER)) {
            return true; // El dueño puede crear cualquier rol excepto Root
        } else if (userRole.equals(Role.ADMIN)) {
            return newUserRole.equals(Role.CASHIER); // Los administradores solo pueden crear cajeros
        }
        return false; // Otros roles no pueden crear usuarios
    }
}
