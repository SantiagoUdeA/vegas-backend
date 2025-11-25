package com.vegas.sistema_gestion_operativa.products_inventory.application.dto;

import com.vegas.sistema_gestion_operativa.common.domain.MovementReason;
import com.vegas.sistema_gestion_operativa.common.domain.Quantity;
import jakarta.validation.constraints.NotNull;

public record ProductAdjustmentDto(

        @NotNull(message = "El ID del producto es obligatorio")
        Long productId,

        @NotNull(message = "La cantidad es obligatoria")
        Quantity quantity,

        @NotNull(message = "La justificación es obligatoria")
        String justification,

        @NotNull(message = "El motivo del ajuste es obligatorio")
        MovementReason movementReason
) {

}

