package com.vegas.sistema_gestion_operativa.inventory.raw_material.appliaction.dto;

import com.vegas.sistema_gestion_operativa.common.domain.Money;
import com.vegas.sistema_gestion_operativa.common.domain.Quantity;
import com.vegas.sistema_gestion_operativa.common.domain.UnitOfMeasure;

import java.time.LocalDateTime;
import java.util.Date;

public record RawMaterialBatchDto(
        Long id,
        String batchNumber,
        Quantity quantity,
        UnitOfMeasure unitOfMeasure,
        Money totalCost,
        LocalDateTime entryDate,
        Date expirationDate,
        Long branchId,
        Long rawMaterialId,
        Long providerId
) {
}
