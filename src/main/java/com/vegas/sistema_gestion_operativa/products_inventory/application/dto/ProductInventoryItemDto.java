package com.vegas.sistema_gestion_operativa.products_inventory.application.dto;

import java.time.LocalDateTime;

public interface ProductInventoryItemDto {
    Long getId();
    Long getProductId();
    String getProductName();
    String getCategoryName();
    Double getCurrentStock();
    Double getAverageCost();
    LocalDateTime getUpdatedAt();
}

