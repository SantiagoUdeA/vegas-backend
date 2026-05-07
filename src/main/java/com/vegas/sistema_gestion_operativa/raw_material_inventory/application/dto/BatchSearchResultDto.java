package com.vegas.sistema_gestion_operativa.raw_material_inventory.application.dto;

import com.vegas.sistema_gestion_operativa.common.domain.Quantity;
import com.vegas.sistema_gestion_operativa.common.domain.UnitOfMeasure;

import java.time.LocalDateTime;
import java.util.Date;

public record BatchSearchResultDto(
        Long id,
        String batchNumber,
        String rawMaterialName,
        UnitOfMeasure unitOfMeasure,
        Quantity initialQuantity,
        Quantity availableQuantity,
        LocalDateTime entryDate,
        Date expirationDate,
        String providerName
) {}
