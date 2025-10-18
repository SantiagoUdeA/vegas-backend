// src/main/java/com/vegas/sistema_gestion_operativa/branches/dto/CreateBranchDto.java
package com.vegas.sistema_gestion_operativa.branches.application.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record CreateBranchDto(
        @NotNull(message = "El nombre de la sucursal es obligatorio")
        @Length(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
        String name,

        @Nullable
        @Length(min = 5, max = 200, message = "La dirección debe tener entre 5 y 200 caracteres")
        String address,

        // TODO Centralizar logica de validación del numero teléfonico
        @Nullable
        @Length(min = 7, max = 20, message = "El número de teléfono debe tener entre 7 y 20 caracteres")
        String phoneNumber
) {
}
