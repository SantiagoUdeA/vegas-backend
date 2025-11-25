package com.vegas.sistema_gestion_operativa.products_inventory.application.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record ProductValuationResponseDto(
        List<ProductValuationItemDto> items,
        Map<String, BigDecimal> valuationByCategory,
        BigDecimal totalValuation
) {
}