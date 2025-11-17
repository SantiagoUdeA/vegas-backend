package com.vegas.sistema_gestion_operativa.production.application.dto;

import com.vegas.sistema_gestion_operativa.catalog.products.api.ProductDto;
import com.vegas.sistema_gestion_operativa.production.application.api.RecipeDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductWithRecipeDto extends ProductDto {

    private RecipeDto recipe;

    public ProductWithRecipeDto(Long id, String name, String unitOfMeasure, RecipeDto recipe) {
        super(id, name, unitOfMeasure, null);
        this.recipe = recipe;
    }
}
