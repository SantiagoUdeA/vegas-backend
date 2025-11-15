package com.vegas.sistema_gestion_operativa.catalog.products.application.dto;

import com.vegas.sistema_gestion_operativa.common.domain.UnitOfMeasure;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record UpdateProductDto(
        @NotNull(message = "El nombre del producto es obligatorio")
        @Length(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
        String name,

        @NotNull(message = "La unidad de medida es obligatoria")
        UnitOfMeasure unitOfMeasure,

        Long categoryId
) {}

