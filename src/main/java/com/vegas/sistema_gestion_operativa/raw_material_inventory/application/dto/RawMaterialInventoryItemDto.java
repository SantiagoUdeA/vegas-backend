package com.vegas.sistema_gestion_operativa.raw_material_inventory.application.dto;

import java.time.LocalDateTime;

public interface RawMaterialInventoryItemDto {
    Long getId();
    String getRawMaterialName();
    String getCategoryName();
    String getUnitOfMeasure();
    Double getCurrentStock();
    Double getAverageCost();
    LocalDateTime getUpdatedAt();
}