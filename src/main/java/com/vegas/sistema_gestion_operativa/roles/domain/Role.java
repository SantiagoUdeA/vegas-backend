package com.vegas.sistema_gestion_operativa.roles.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;

import static com.vegas.sistema_gestion_operativa.roles.domain.Permission.*;

@Getter
public enum Role {

    ADMIN(
            "Administrador de una sede. Tiene acceso a los usuarios, inventario y reportes de una sede.",
            Set.of(
                    CASHIERS_CREATE, CASHIERS_EDIT, CASHIERS_DEACTIVATE, CASHIERS_VIEW,
                    INVENTORY_CREATE, INVENTORY_EDIT, INVENTORY_DELETE, INVENTORY_VIEW,
                    PRODUCTS_CREATE, PRODUCTS_EDIT, PRODUCTS_DELETE, PRODUCTS_VIEW,
                    FORMULAS_CREATE, FORMULAS_EDIT, FORMULAS_VIEW,
                    PROVIDERS_CREATE, PROVIDERS_EDIT, PROVIDERS_VIEW, PROVIDERS_DELETE,
                    RAW_MATERIALS_CREATE, RAW_MATERIALS_EDIT, RAW_MATERIALS_DELETE, RAW_MATERIALS_VIEW,
                    REPORTS_VIEW, REPORTS_EXPORT,
                    BRANCHES_VIEW,
                    SALES_CREATE, SALES_READ
            )
    ),

    CASHIER(
            "Usuario de punto de venta con acceso limitado a operaciones básicas de venta e inventario.",
            Set.of(
                    INVENTORY_VIEW,
                    PRODUCTS_VIEW,
                    RAW_MATERIALS_VIEW,
                    REPORTS_VIEW,
                    DASHBOARD_VIEW,
                    PROVIDERS_CREATE, PROVIDERS_EDIT, PROVIDERS_VIEW, PROVIDERS_DELETE,
                    SALES_CREATE, SALES_READ
            )
    ),

    OWNER(
            "Dueño de una sede.",
            Set.of(
                    ADMINS_CREATE, ADMINS_EDIT, ADMINS_DEACTIVATE, ADMINS_VIEW,
                    CASHIERS_CREATE, CASHIERS_EDIT, CASHIERS_DEACTIVATE, CASHIERS_VIEW,
                    OWNERS_CREATE, OWNERS_EDIT, OWNERS_DEACTIVATE, OWNERS_VIEW,
                    FORMULAS_CREATE, FORMULAS_EDIT, FORMULAS_VIEW,
                    INVENTORY_CREATE, INVENTORY_EDIT, INVENTORY_DELETE, INVENTORY_VIEW,
                    PRODUCTS_CREATE, PRODUCTS_EDIT, PRODUCTS_DELETE, PRODUCTS_VIEW,
                    PROVIDERS_CREATE, PROVIDERS_EDIT, PROVIDERS_DELETE, PROVIDERS_VIEW,
                    RAW_MATERIALS_CREATE, RAW_MATERIALS_EDIT, RAW_MATERIALS_DELETE, RAW_MATERIALS_VIEW,
                    REPORTS_VIEW, REPORTS_EXPORT,
                    ALERTS_VIEW, ALERTS_CONFIGURE,
                    DASHBOARD_VIEW,
                    BRANCHES_CREATE, BRANCHES_EDIT, BRANCHES_DELETE, BRANCHES_VIEW,
                    SALES_READ
            )
    ),

    ROOT(
            "Acceso total e irrestricto a todas las funcionalidades del sistema, incluyendo configuración avanzada y auditorías.",
            Set.of(
                    OWNERS_CREATE, OWNERS_EDIT, OWNERS_DEACTIVATE, OWNERS_VIEW
            )
    );

    private final String description;
    private final Set<Permission> permissions;

    Role(String description, Set<Permission> permissions) {
        this.description = description;
        this.permissions = permissions;
    }

    @JsonCreator
    public static Role fromValue(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El rol no puede ser nulo o vacío");
        }

        try {
            return Role.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            String allowed = Arrays.toString(Role.values());
            throw new IllegalArgumentException(
                    String.format("El rol '%s' no es válido. Valores permitidos: %s", value, allowed)
            );
        }
    }

}
