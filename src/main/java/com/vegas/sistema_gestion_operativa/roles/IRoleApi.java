package com.vegas.sistema_gestion_operativa.roles;

public interface IRoleApi {

    boolean hasPermission(String roleName, String permission);
    boolean canManageUserWithRole(String userRoleName, String targetRoleName);
}
