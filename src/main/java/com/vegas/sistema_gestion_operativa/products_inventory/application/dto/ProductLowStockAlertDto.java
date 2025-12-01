package com.vegas.sistema_gestion_operativa.products_inventory.application.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ProductLowStockAlertDto {
    private Long inventoryId;
    private Long productId;
    private String productName;
    private String categoryName;
    private Double currentStock;
    private Long branchId;
    private LocalDateTime updatedAt;
}