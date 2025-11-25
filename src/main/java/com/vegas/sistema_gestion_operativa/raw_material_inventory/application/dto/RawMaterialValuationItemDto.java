package com.vegas.sistema_gestion_operativa.raw_material_inventory.application.dto;

public interface RawMaterialValuationItemDto {
    Long getRawMaterialId();

    String getRawMaterialName();

    String getCategoryName();

    Double getCurrentStock();

    Double getAverageCost();

    Double getValuation();
}
