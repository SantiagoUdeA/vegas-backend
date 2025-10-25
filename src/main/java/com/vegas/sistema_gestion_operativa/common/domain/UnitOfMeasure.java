package com.vegas.sistema_gestion_operativa.common.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum UnitOfMeasure {
    KILOGRAM("Kilogramo", "kg"),
    GRAM("Gramo", "g"),
    LITER("Litro", "L"),
    METER("Metro", "m");

    private final String name;
    private final String symbol;

    UnitOfMeasure(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    @JsonCreator
    public static UnitOfMeasure fromValue(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("La unidad de medida no puede ser nula o vacío");
        }

        try {
            return UnitOfMeasure.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            String allowed = Arrays.toString(UnitOfMeasure.values());
            throw new IllegalArgumentException(
                    String.format("La unidad de medida no es valida '%s' no es válido. Valores permitidos: %s", value, allowed)
            );
        }
    }

}
