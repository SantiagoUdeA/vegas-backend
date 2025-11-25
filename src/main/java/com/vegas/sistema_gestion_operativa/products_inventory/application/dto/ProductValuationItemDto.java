package com.vegas.sistema_gestion_operativa.products_inventory.application.dto;

import java.math.BigDecimal;

public record ProductValuationItemDto(
        Long productId,
        String productName,
        String categoryName,
        BigDecimal currentStock,
        BigDecimal averageCost,
        BigDecimal valuation
) {
}
