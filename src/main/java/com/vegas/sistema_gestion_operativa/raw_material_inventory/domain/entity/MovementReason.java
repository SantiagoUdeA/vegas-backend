package com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.vegas.sistema_gestion_operativa.roles.domain.Role;

import java.util.Arrays;

public enum MovementReason {
    PRODUCT_ENTRY,
    SALIDA,
    ENTRY,
    PRODUCTION,
    PURCHASE,
    CONSUMPTION,
    ADJUSTMENT,
    TRANSFER,
    RETURN;


    @JsonCreator
    public static MovementReason fromValue(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El movimiento no puede ser nulo o vacío");
        }

        try {
            return MovementReason.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            String allowed = Arrays.toString(Role.values());
            throw new IllegalArgumentException(
                    String.format("El movimiento '%s' no es válido. Valores permitidos: %s", value, allowed)
            );
        }
    }
}
