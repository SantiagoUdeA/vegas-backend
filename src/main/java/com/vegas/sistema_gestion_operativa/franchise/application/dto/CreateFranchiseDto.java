package com.vegas.sistema_gestion_operativa.franchise.application.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record CreateFranchiseDto(
        @NotNull(message = "El nombre de la franquicia es obligatorio")
        @Length(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
        String name,

        @Nullable
        @Length(min = 3, max = 100, message = "El slug debe tener entre 3 y 100 caracteres")
        String slug
) {
}
