package com.vegas.sistema_gestion_operativa.products_inventory.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RegisterProductStockDto(
     @NotNull(message = "El ID de la sucursal es obligatorio")
     Long branchId,

    @NotNull(message = "El ID del producto es obligatorio")
    Long productId,

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a cero")
    Integer quantity
) {
}

