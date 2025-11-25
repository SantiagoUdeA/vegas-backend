package com.vegas.sistema_gestion_operativa.products.api;

import com.vegas.sistema_gestion_operativa.common.domain.Money;
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
    private ProductCategoryDto category;
    private Money price;
    private RecipeDto recipe;
}

