package com.vegas.sistema_gestion_operativa.catalog.products.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String unitOfMeasure;
    private ProductCategoryDto category;
}

