package com.vegas.sistema_gestion_operativa.sales.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ProductSalesStatsDto {
    private Long productId;
    private String productName;
    private BigDecimal totalQuantitySold;
}
