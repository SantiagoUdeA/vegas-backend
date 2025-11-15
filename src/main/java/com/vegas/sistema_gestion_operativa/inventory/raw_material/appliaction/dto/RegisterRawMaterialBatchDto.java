package com.vegas.sistema_gestion_operativa.inventory.raw_material.appliaction.dto;

import com.vegas.sistema_gestion_operativa.common.domain.UnitOfMeasure;
import com.vegas.sistema_gestion_operativa.inventory.raw_material.domain.entity.MovementReason;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Date;

public record RegisterRawMaterialBatchDto(
    @NotNull(message = "El ID de la materia prima es obligatorio")
    Long rawMaterialId,

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a cero")
    Integer quantity,

    @NotNull(message = "El costo total es obligatorio")
    @Positive(message = "El costo total debe ser mayor a cero")
    Double totalCost,

    @NotNull(message = "La unidad de medida es obligatoria")
    UnitOfMeasure unitOfMeasure,

    @NotNull(message = "La fecha de expiración es obligatoria")
    Date expirationDate,

    @NotBlank(message = "El número de lote es obligatorio")
    String batchNumber,

    @NotNull(message = "El ID del proveedor es obligatorio")
    Long providerId,

    @NotNull(message = "El ID de la sucursal es obligatorio")
    Long branchId,

    @NotNull(message = "La razón del movimiento es obligatoria")
    MovementReason movementReason
) {
}
