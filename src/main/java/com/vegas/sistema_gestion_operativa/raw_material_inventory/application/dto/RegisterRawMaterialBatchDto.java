package com.vegas.sistema_gestion_operativa.raw_material_inventory.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Date;

public record RegisterRawMaterialBatchDto(
    @NotBlank(message = "El número de lote es obligatorio")
    String batchNumber,

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a cero")
    Double quantity,

    @NotNull(message = "El costo total es obligatorio")
    @Positive(message = "El costo total debe ser mayor a cero")
    Double totalCost,

    @NotNull(message = "La fecha de expiración es obligatoria")
    Date expirationDate,

    @NotNull(message = "El ID del proveedor es obligatorio")
    Long providerId,

    @NotNull(message = "El ID de la sucursal es obligatorio")
    Long branchId,

    @NotNull(message = "El ID de la materia prima es obligatorio")
    Long rawMaterialId

) {
}
