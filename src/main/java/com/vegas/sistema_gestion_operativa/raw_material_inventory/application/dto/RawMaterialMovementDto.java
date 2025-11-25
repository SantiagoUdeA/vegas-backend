package com.vegas.sistema_gestion_operativa.raw_material_inventory.application.dto;

import com.vegas.sistema_gestion_operativa.common.domain.MovementReason;
import com.vegas.sistema_gestion_operativa.common.domain.Quantity;

import java.time.LocalDateTime;

public record RawMaterialMovementDto(
        Long id,
        Quantity quantity,
        LocalDateTime movementDate,
        MovementReason movementReason,
        String rawMaterialBatchCode,
        String rawMaterialName,
        String userName,
        String justification
) {
}
