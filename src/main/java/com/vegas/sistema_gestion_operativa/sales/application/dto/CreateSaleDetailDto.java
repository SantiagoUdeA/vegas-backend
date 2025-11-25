package com.vegas.sistema_gestion_operativa.sales.application.dto;

import com.vegas.sistema_gestion_operativa.common.domain.Quantity;
import jakarta.validation.constraints.NotNull;

public record CreateSaleDetailDto(
        @NotNull(message = "El ID del producto es obligatorio")
        Long productId,

        @NotNull(message = "La cantidad es obligatoria")
        Quantity quantity
) {

}