package com.vegas.sistema_gestion_operativa.raw_material_inventory.application.dto;

import java.util.List;
import java.util.Map;

public record RawMaterialValuationResponseDto(
        Double totalValuation,
        Map<String, Double> valuationByCategory,
        List<RawMaterialValuationItemDto> items
) {}