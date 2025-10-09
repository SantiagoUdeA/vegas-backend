package com.vegas.sistema_gestion_operativa.roles.factory;

import com.vegas.sistema_gestion_operativa.roles.domain.Role;

public class RoleFactory {

    public static Role createRole(String roleName) {
        return switch (roleName.toUpperCase()) {
            case "ADMIN" -> Role.ADMIN;
            case "CASHIER" -> Role.CASHIER;
            case "OWNER" -> Role.OWNER;
            default -> throw new IllegalArgumentException("Invalid role ID: " + roleName);
        };
    }
}
