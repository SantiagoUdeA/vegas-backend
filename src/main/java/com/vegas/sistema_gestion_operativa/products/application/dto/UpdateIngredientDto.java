package com.vegas.sistema_gestion_operativa.products.application.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;

public record UpdateIngredientDto(

        @Nullable
        @Positive(message = "La cantidad debe ser mayor a cero")
        Double quantity,

        @Nullable
        @Length(max = 500, message = "Las observaciones no pueden superar los 500 caracteres")
        String observations,

        @Nullable
        Long rawMaterialId
) {
}
