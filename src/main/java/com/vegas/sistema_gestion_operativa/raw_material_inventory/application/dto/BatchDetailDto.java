package com.vegas.sistema_gestion_operativa.raw_material_inventory.application.dto;

import com.vegas.sistema_gestion_operativa.common.domain.Money;
import com.vegas.sistema_gestion_operativa.common.domain.Quantity;
import com.vegas.sistema_gestion_operativa.common.domain.UnitOfMeasure;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Date;

public record BatchDetailDto(
        Long id,
        String batchNumber,
        Long rawMaterialId,
        String rawMaterialName,
        UnitOfMeasure unitOfMeasure,
        Quantity initialQuantity,
        Quantity availableQuantity,
        Money totalCost,
        LocalDateTime entryDate,
        Date expirationDate,
        Long providerId,
        String providerName,
        Long branchId
) {
    /**
     * Calculated unit cost: totalCost / initialQuantity.
     * Not persisted; computed on the fly for the response.
     */
    public BigDecimal unitCost() {
        if (initialQuantity == null || initialQuantity.getValue().compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return totalCost.getValue().divide(initialQuantity.getValue(), 4, RoundingMode.HALF_UP);
    }
}
