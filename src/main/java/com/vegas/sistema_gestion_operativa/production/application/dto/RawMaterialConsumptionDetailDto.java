package com.vegas.sistema_gestion_operativa.production.application.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RawMaterialConsumptionDetailDto {

    private Long rawMaterialId;
    private String rawMaterialName;
    private BigDecimal totalConsumed;
    private List<BatchConsumptionDetailDto> batchesConsumed;
}
