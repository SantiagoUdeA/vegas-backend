package com.vegas.sistema_gestion_operativa.common.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum MovementReason {
    ENTRADA,
    SALIDA,
    PRODUCCION,
    COMPRA,
    AUTOCONSUMO,
    RETORNO,
    AJUSTE_POR_MERMA,
    AJUSTE_POR_PERDIDA_POR_ROBO,
    AJUSTE_POR_ERROR_DE_CONTEO,


    PRODUCT_ENTRY;

    @JsonCreator
    public static MovementReason fromValue(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El movimiento no puede ser nulo o vacío");
        }

        try {
            return MovementReason.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            String allowed = Arrays.toString(MovementReason.values());
            throw new IllegalArgumentException(
                    String.format("El movimiento '%s' no es válido. Valores permitidos: %s", value, allowed)
            );
        }
    }
}
