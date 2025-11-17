package com.vegas.sistema_gestion_operativa.production.application.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public record CreateRecipeDto(

        @NotNull(message = "Las unidades producidas son obligatorias")
        @Min(value = 1, message = "Las unidades producidas deben ser al menos 1")
        Integer unitsProduced,

        @Nullable
        @Length(max = 500, message = "Las notas no pueden superar los 500 caracteres")
        String observations,

        @NotNull(message = "El ID del producto es obligatorio")
        Long productId,

        @NotNull(message = "Los ingredientes son obligatorios")
        @NotEmpty(message = "Debe incluir al menos un ingrediente")
        @Valid
        List<CreateIngredientDto> ingredients
) {
}
