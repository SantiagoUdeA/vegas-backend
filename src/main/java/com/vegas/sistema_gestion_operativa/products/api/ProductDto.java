package com.vegas.sistema_gestion_operativa.products.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private ProductCategoryDto category;
    private BigDecimal price;
    private RecipeDto recipe;
}

