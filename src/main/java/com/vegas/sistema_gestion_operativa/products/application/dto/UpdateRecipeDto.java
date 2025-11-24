package com.vegas.sistema_gestion_operativa.products.application.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public record UpdateRecipeDto(
        @Nullable
        @Min(value = 1, message = "Las unidades producidas deben ser al menos 1")
        Integer unitsProduced,

        @Nullable
        @Length(max = 500, message = "Las notas no pueden superar los 500 caracteres")
        String observations,

        @Nullable
        @NotEmpty(message = "Debe incluir al menos un ingrediente")
        @Valid
        List<UpdateIngredientDto> ingredients
) {
}
