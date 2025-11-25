package com.vegas.sistema_gestion_operativa.sales.application.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateSaleDto(
        @NotNull(message = "El ID de la sucursal es obligatorio")
        Long branchId,
        
        @NotEmpty(message = "La lista de detalles de venta no puede estar vacía")
        List<CreateSaleDetailDto> details
) {
}
