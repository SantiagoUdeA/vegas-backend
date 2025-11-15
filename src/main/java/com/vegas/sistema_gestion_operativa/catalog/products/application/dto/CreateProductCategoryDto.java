package com.vegas.sistema_gestion_operativa.catalog.products.application.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record CreateProductCategoryDto(
        @NotNull(message = "El nombre de la categoría es obligatorio")
        @Length(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
        String name,

        @Length(max = 255, message = "La descripción no puede exceder 255 caracteres")
        String description
) {}

