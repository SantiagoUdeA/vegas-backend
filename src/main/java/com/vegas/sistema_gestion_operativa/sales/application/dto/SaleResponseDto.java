package com.vegas.sistema_gestion_operativa.sales.application.dto;

import com.vegas.sistema_gestion_operativa.common.domain.Money;
import com.vegas.sistema_gestion_operativa.common.domain.Quantity;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder
public class SaleResponseDto {

    Long id;
    LocalDateTime saleDate;
    Money total;
    String employeeId;
    String employeeName;
    Long branchId;
    List<DetailDto> details;

    @Value
    @Builder
    public static class DetailDto {
        Long id;
        Long productId;
        String productName;
        Quantity quantity;
        Money unitPrice;
        Money subtotal;
    }
}