package com.vegas.sistema_gestion_operativa.raw_material_inventory.application.dto;

import com.vegas.sistema_gestion_operativa.common.domain.Quantity;

import java.time.LocalDateTime;

public record ProductionReferenceDto(
        Long productionId,
        String productName,
        Quantity quantityProduced,
        LocalDateTime productionDate
) {}
