package com.vegas.sistema_gestion_operativa.roles.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum Permission {

    // Sucursales
    BRANCHES_CREATE,
    BRANCHES_EDIT,
    BRANCHES_DELETE,
    BRANCHES_VIEW,

    // Administradores
    ADMINS_CREATE,
    ADMINS_EDIT,
    ADMINS_DEACTIVATE,
    ADMINS_VIEW,

    // Cajeros
    CASHIERS_CREATE,
    CASHIERS_EDIT,
    CASHIERS_DEACTIVATE,
    CASHIERS_VIEW,

    // Dueños
    OWNERS_CREATE,
    OWNERS_EDIT,
    OWNERS_DEACTIVATE,
    OWNERS_VIEW,

    // Proveedores
    PROVIDERS_CREATE,
    PROVIDERS_EDIT,
    PROVIDERS_DELETE,
    PROVIDERS_VIEW,

    // Materias Primas
    RAW_MATERIALS_CREATE,
    RAW_MATERIALS_EDIT,
    RAW_MATERIALS_DELETE,
    RAW_MATERIALS_VIEW,

    // Inventario
    INVENTORY_CREATE,
    INVENTORY_EDIT,
    INVENTORY_DELETE,
    INVENTORY_VIEW,

    // Productos
    PRODUCTS_CREATE,
    PRODUCTS_EDIT,
    PRODUCTS_DELETE,
    PRODUCTS_VIEW,

    // Reportes
    REPORTS_VIEW,
    REPORTS_EXPORT,

    // Producción
    FORMULAS_CREATE,
    FORMULAS_EDIT,
    FORMULAS_VIEW,

    // Alertas
    ALERTS_VIEW,
    ALERTS_CONFIGURE,

    // Ventas
    SALES_CREATE,
    SALES_READ,

    // Dashboard
    DASHBOARD_VIEW;

    @JsonCreator
    public static Permission fromValue(String value) throws  IllegalArgumentException {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El permiso no puede ser nulo o vacío");
        }
        try {
            return Permission.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            String allowed = Arrays.toString(Permission.values());
            throw new IllegalArgumentException(
                    String.format("El permiso '%s' no es válido. Valores permitidos: %s", value, allowed)
            );
        }
    }
}
