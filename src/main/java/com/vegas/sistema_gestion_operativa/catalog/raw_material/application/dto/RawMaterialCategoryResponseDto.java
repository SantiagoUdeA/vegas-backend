package com.vegas.sistema_gestion_operativa.catalog.raw_material.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RawMaterialCategoryResponseDto {
    private Long id;
    private String name;
    private String description;
}

