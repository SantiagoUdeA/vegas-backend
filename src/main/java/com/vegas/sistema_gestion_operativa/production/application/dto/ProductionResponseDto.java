package com.vegas.sistema_gestion_operativa.production.application.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductionResponseDto {

    private Long productionId;
    private Long productId;
    private String productName;
    private BigDecimal quantityProduced;
    private LocalDateTime productionDate;
    private List<RawMaterialConsumptionDetailDto> rawMaterialsConsumed;
}
