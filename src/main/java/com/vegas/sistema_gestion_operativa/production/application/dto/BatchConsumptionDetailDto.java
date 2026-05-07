package com.vegas.sistema_gestion_operativa.production.application.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchConsumptionDetailDto {

    private Long batchId;
    private String batchNumber;
    private BigDecimal quantityConsumed;
}
