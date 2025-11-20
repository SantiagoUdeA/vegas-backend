package com.vegas.sistema_gestion_operativa.products_inventory.application.dto;

import java.time.LocalDateTime;

public record ProductInventoryResponseDto(
    Long id,
    Long productId,
    Double currentStock,
    Double averageCost,
    LocalDateTime updatedAt
) {
}

