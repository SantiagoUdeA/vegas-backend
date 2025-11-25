package com.vegas.sistema_gestion_operativa.products_inventory.application.dto;

import jakarta.validation.constraints.NotNull;

public record RegisterProductStockDto(
        @NotNull(message = "El ID de la sucursal es obligatorio")
        Long branchId,

        @NotNull(message = "El ID del producto es obligatorio")
        Long productId,

        @NotNull(message = "La cantidad es obligatoria")
        Integer quantity,

        Double cost  // ← nuevo campo opcional
) {
}
