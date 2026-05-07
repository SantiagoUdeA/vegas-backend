package com.vegas.sistema_gestion_operativa.production.application.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RawMaterialShortageDto {

    private Long rawMaterialId;
    private String rawMaterialName;
    private String unitOfMeasure;
    private BigDecimal required;
    private BigDecimal available;
    private BigDecimal deficit;
}
