package com.vegas.sistema_gestion_operativa.products_inventory.application.dto;


import com.vegas.sistema_gestion_operativa.common.domain.Quantity;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.entity.MovementReason;

import java.time.LocalDateTime;

public record ProductInventoryMovementDto(
        Long id,
        String productName,
        String userName,
        Quantity quantity,
        MovementReason movementReason,
        String justification,
        LocalDateTime movementDate
) {}