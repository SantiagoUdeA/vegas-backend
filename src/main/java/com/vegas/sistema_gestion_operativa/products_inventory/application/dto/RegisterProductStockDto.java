package com.vegas.sistema_gestion_operativa.products_inventory.application.dto;

import com.vegas.sistema_gestion_operativa.common.domain.Quantity;
import jakarta.validation.constraints.NotNull;

public record RegisterProductStockDto(
        @NotNull(message = "El ID de la sucursal es obligatorio")
        Long branchId,

        @NotNull(message = "El ID del producto es obligatorio")
        Long productId,

        @NotNull(message = "La cantidad es obligatoria")
        Quantity quantity,

        Double cost  // ← nuevo campo opcional
) {
}
