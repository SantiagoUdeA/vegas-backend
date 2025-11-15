package com.vegas.sistema_gestion_operativa.inventory.raw_material.appliaction.dto;

import java.time.LocalDateTime;

public interface RawMaterialInventoryItemDto {
    String getRawMaterialName();
    String getCategoryName();
    String getUnitOfMeasure();
    Double getCurrentStock();
    LocalDateTime getUpdatedAt();
}