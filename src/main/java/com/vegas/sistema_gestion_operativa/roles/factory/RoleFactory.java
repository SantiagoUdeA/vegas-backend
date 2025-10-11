package com.vegas.sistema_gestion_operativa.roles.factory;

import com.vegas.sistema_gestion_operativa.roles.domain.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleFactory {

    public Role createRole(String roleName) throws IllegalArgumentException {
        return switch (roleName.toUpperCase()) {
            case "ADMIN" -> Role.ADMIN;
            case "CASHIER" -> Role.CASHIER;
            case "OWNER" -> Role.OWNER;
            default -> throw new IllegalArgumentException("Invalid role ID: " + roleName);
        };
    }
}
