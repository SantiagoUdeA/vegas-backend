package com.vegas.sistema_gestion_operativa.reports.domain.entity;

import com.vegas.sistema_gestion_operativa.common.domain.MovementReason;
import com.vegas.sistema_gestion_operativa.common.domain.Quantity;

import java.time.LocalDateTime;

public record MovementItem(
        String itemName,
        String itemCategoryName,
        String userName,
        Quantity quantity,
        MovementReason movementReason,
        String justification,
        LocalDateTime movementDate
) {
}