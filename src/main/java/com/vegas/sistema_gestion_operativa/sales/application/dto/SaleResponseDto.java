package com.vegas.sistema_gestion_operativa.sales.application.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder
public class SaleResponseDto {

    Long id;
    LocalDateTime saleDate;
    BigDecimal total;
    String employeeId;
    Long branchId;
    List<DetailDto> details;

    @Value
    @Builder
    public static class DetailDto {
        Long id;
        Long productId;
        Integer quantity;
        BigDecimal unitPrice;
        BigDecimal subtotal;
    }
}