package com.vegas.sistema_gestion_operativa.raw_material_inventory.application.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RawMaterialLowStockAlertDto {
    private Long inventoryId;
    private Long rawMaterialId;
    private String rawMaterialName;
    private Double currentStock;
    private Long branchId;
    private String unitName;
    private String updatedAt;
}