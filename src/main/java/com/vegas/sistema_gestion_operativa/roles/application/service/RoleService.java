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

    /**
     * Verifica si un usuario con un rol específico puede crear o editar usuarios con otro rol.
     *
     * @param userRoleName   el nombre del rol del usuario que realiza la acción
     * @param targetRoleName el nombre del rol del usuario objetivo
     * @return true si el usuario puede gestionar el rol objetivo, false en caso contrario
     */
    public boolean canManageUserWithRole(String userRoleName, String targetRoleName) {
        Role userRole;
        Role targetRole;
        try {
            userRole = Role.fromValue(userRoleName);
            targetRole = Role.fromValue(targetRoleName);
        } catch (IllegalArgumentException e) {
            return false; // Rol no válido
        }

        return switch (userRole) {
            case ROOT -> targetRole.equals(Role.OWNER); // ROOT solo puede gestionar OWNER
            case OWNER ->
                    targetRole.equals(Role.ADMIN) || targetRole.equals(Role.CASHIER); // OWNER puede gestionar ADMIN y CASHIER
            case ADMIN -> targetRole.equals(Role.CASHIER); // ADMIN solo puede gestionar CASHIER
            default -> false;
        };
    }

    @Override
    public boolean isCashierRole(String roleName) {
        return this.isRole(roleName, Role.CASHIER);
    }

    @Override
    public boolean isOwnerRole(String roleName) {
        return this.isRole(roleName, Role.OWNER);
    }

    @Override
    public boolean isAdminRole(String roleName) {
        return this.isRole(roleName, Role.ADMIN);
    }

    @Override
    public boolean isRootRole(String roleName) {
        return this.isRole(roleName, Role.ROOT);
    }

    private boolean isRole(String roleName, Role targetRole) {
        Role role;
        try {
            role = Role.fromValue(roleName);
        } catch (IllegalArgumentException e) {
            return false; // Rol no válido
        }
        return role == targetRole;
    }
}
