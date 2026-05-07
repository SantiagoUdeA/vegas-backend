package com.vegas.sistema_gestion_operativa.raw_material_inventory.application.dto;

import com.vegas.sistema_gestion_operativa.common.domain.MovementReason;
import com.vegas.sistema_gestion_operativa.common.domain.Quantity;

import java.time.LocalDateTime;

public record BatchMovementItemDto(
        Long movementId,
        LocalDateTime movementDate,
        MovementReason movementReason,
        Quantity quantity,
        Quantity balanceAfter,
        String userName,
        String justification,
        ProductionReferenceDto productionReference
) {}
