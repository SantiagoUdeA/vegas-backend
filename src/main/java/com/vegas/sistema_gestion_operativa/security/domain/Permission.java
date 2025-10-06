package com.vegas.sistema_gestion_operativa.security.domain;

public enum Permission {

    // Sucursales
    BRANCHES_CREATE,
    BRANCHES_EDIT,
    BRANCHES_DELETE,
    BRANCHES_VIEW,

    // Usuarios
    USERS_CREATE,
    USERS_EDIT,
    USERS_DEACTIVATE,
    USERS_VIEW,

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

    // Dashboard
    DASHBOARD_VIEW
}
