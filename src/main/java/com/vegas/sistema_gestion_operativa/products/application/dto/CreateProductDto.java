package com.vegas.sistema_gestion_operativa.products.application.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record CreateProductDto(
        @NotNull(message = "El nombre del producto es obligatorio")
        @Length(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
        String name,

        @NotNull(message = "La categoría es obligatoria")
        Long categoryId
) {}

