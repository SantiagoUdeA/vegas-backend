package com.vegas.sistema_gestion_operativa.reports.domain.entity;

import com.vegas.sistema_gestion_operativa.common.domain.Money;
import com.vegas.sistema_gestion_operativa.common.domain.Quantity;
import com.vegas.sistema_gestion_operativa.common.domain.UnitOfMeasure;

import java.time.LocalDateTime;

public record RawMaterialInventoryItem(
        String itemName,
        String itemCategoryName,
        Quantity currentStock,
        LocalDateTime updatedAt,
        Money averageCost,
        UnitOfMeasure unitOfMeasure
) {
}
