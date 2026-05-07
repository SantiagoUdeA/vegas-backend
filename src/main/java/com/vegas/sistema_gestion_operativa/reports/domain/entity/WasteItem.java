package com.vegas.sistema_gestion_operativa.reports.domain.entity;

import com.vegas.sistema_gestion_operativa.common.domain.Money;
import com.vegas.sistema_gestion_operativa.common.domain.MovementReason;
import com.vegas.sistema_gestion_operativa.common.domain.Quantity;
import com.vegas.sistema_gestion_operativa.common.domain.UnitOfMeasure;

import java.time.LocalDateTime;

public record WasteItem(
        String itemName,
        String itemType,
        MovementReason movementReason,
        Quantity quantity,
        UnitOfMeasure unitOfMeasure,
        Money totalValue,
        String justification,
        LocalDateTime movementDate
) {
}
