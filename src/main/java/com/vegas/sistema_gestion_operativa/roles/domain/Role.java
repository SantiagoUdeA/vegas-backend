package com.vegas.sistema_gestion_operativa.roles.domain;

import lombok.Getter;

import java.util.Set;

import static com.vegas.sistema_gestion_operativa.roles.domain.Permission.*;

@Getter
public enum Role {

    ADMIN(
            "Administrador general del sistema. Tiene acceso total a la configuración, usuarios, inventario y reportes.",
            Set.of(
                    USERS_CREATE, USERS_EDIT, USERS_DEACTIVATE, USERS_VIEW,
                    INVENTORY_CREATE, INVENTORY_EDIT, INVENTORY_DELETE, INVENTORY_VIEW,
                    PRODUCTS_CREATE, PRODUCTS_EDIT, PRODUCTS_DELETE, PRODUCTS_VIEW,
                    FORMULAS_CREATE, FORMULAS_EDIT, FORMULAS_VIEW,
                    REPORTS_VIEW, REPORTS_EXPORT,
                    ALERTS_VIEW, ALERTS_CONFIGURE,
                    DASHBOARD_VIEW,
                    BRANCHES_CREATE, BRANCHES_EDIT, BRANCHES_DELETE, BRANCHES_VIEW
            )
    ),

    CASHIER(
            "Usuario de punto de venta con acceso limitado a operaciones básicas de venta e inventario.",
            Set.of(
                    INVENTORY_VIEW,
                    PRODUCTS_VIEW,
                    REPORTS_VIEW,
                    DASHBOARD_VIEW
            )
    ),

    OWNER(
            "Dueño del negocio con acceso ejecutivo de solo lectura a reportes, dashboard y auditorías.",
            Set.of(
                    USERS_VIEW,
                    REPORTS_VIEW, REPORTS_EXPORT,
                    INVENTORY_VIEW,
                    DASHBOARD_VIEW
            )
    );

    private final String description;
    private final Set<Permission> permissions;

    Role(String description, Set<Permission> permissions) {
        this.description = description;
        this.permissions = permissions;
    }

    public boolean hasPermission(Permission permission) {
        return permissions.contains(permission);
    }
}
