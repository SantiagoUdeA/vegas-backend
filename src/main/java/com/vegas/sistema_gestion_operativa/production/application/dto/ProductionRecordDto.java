package com.vegas.sistema_gestion_operativa.production.application.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductionRecordDto {

    private Long id;
    private String productName;
    private BigDecimal quantityProduced;
    private LocalDateTime productionDate;
}
