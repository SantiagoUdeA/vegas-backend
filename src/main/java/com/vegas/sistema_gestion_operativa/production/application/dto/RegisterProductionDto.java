package com.vegas.sistema_gestion_operativa.production.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record RegisterProductionDto(
        @NotNull(message = "El ID de la sede es obligatorio")
        Long branchId,

        @NotNull(message = "El ID del producto es obligatorio")
        Long productId,

        @NotNull(message = "La cantidad a producir es obligatoria")
        @Positive(message = "La cantidad a producir debe ser mayor a 0")
        BigDecimal quantityToProduce,

        @Size(max = 500, message = "Las observaciones no pueden superar los 500 caracteres")
        String observations
) {
}
