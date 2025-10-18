package com.vegas.sistema_gestion_operativa.roles.application.service;

import com.vegas.sistema_gestion_operativa.roles.IRoleApi;
import com.vegas.sistema_gestion_operativa.roles.domain.Permission;
import com.vegas.sistema_gestion_operativa.roles.domain.Role;
import org.springframework.stereotype.Service;

@Service
public class RoleService implements IRoleApi {

    @Override
    public boolean hasPermission(String roleName, String permissionName) {
        Role role;
        Permission permission;
        try {
            role = Role.fromValue(roleName); // Convierte el nombre del rol a un objeto Role
            permission = Permission.valueOf(permissionName); // Convierte el nombre del permiso a un objeto Permission
        } catch (IllegalArgumentException e) {
            return false; // Rol o permiso no válido
        }
        return role.getPermissions().contains(permission); // Verifica si el rol tiene el permiso
    }

    @Override
    public boolean canCreateUserWithRole(String userRoleName, String newUserRoleName) {
        Role userRole;
        Role newUserRole;
        try {
            userRole = Role.fromValue(userRoleName); // Convierte el nombre del rol del usuario a un objeto Role
            newUserRole = Role.fromValue(newUserRoleName); // Convierte el nombre del nuevo rol a un objeto Role
        } catch (IllegalArgumentException e) {
            return false; // Rol o permiso no válido
        }

        return switch (userRole) {
            case ROOT -> newUserRole.equals(Role.OWNER); // ROOT solo puede crear OWNER
            case OWNER -> newUserRole.equals(Role.ADMIN) || newUserRole.equals(Role.CASHIER); // OWNER puede crear ADMIN y CASHIER
            case ADMIN -> newUserRole.equals(Role.CASHIER); // ADMIN solo puede crear CASHIER
            default -> false;
        };
    }
}
