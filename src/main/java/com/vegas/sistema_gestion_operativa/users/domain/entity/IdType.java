package com.vegas.sistema_gestion_operativa.users.domain.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;

public enum IdType {
    CC, // Cédula de ciudadanía
    CE, // Cédula de extranjería
    PP; // Pasaporte

    @JsonCreator
    public static IdType fromValue(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El tipo de documento no puede ser nulo o vacío");
        }

        try {
            return IdType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            String allowed = Arrays.toString(IdType.values());
            throw new IllegalArgumentException(
                    String.format("El tipo de documento '%s' no es válido. Valores permitidos: %s", value, allowed)
            );
        }
    }
}
