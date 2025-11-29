package com.vegas.sistema_gestion_operativa.products_inventory.api;


import com.vegas.sistema_gestion_operativa.common.domain.MovementReason;
import com.vegas.sistema_gestion_operativa.common.domain.Quantity;

import java.time.LocalDateTime;

public record ProductInventoryMovementDto(
        Long id,
        String productName,
        String productCategoryName,
        String userName,
        Quantity quantity,
        MovementReason movementReason,
        String justification,
        LocalDateTime movementDate
) {
}