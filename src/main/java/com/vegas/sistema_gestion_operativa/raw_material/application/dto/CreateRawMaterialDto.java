package com.vegas.sistema_gestion_operativa.raw_material.application.dto;


import com.vegas.sistema_gestion_operativa.common.domain.UnitOfMeasure;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record CreateRawMaterialDto(
        @NotNull(message = "El nombre de la materia prima es obligatorio")
        @Length(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
        String name,

        @NotNull(message = "La unidad de medida es obligatoria")
        UnitOfMeasure unitOfMeasure,

        @Nullable
        Long categoryId,

        @NotNull(message = "La sede es obligatoria")
        Long branchId
) {}
