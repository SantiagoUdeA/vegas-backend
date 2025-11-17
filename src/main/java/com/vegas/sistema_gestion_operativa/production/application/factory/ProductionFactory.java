package com.vegas.sistema_gestion_operativa.production.application.factory;

import com.vegas.sistema_gestion_operativa.common.domain.Quantity;
import com.vegas.sistema_gestion_operativa.production.application.dto.CreateIngredientDto;
import com.vegas.sistema_gestion_operativa.production.application.dto.CreateRecipeDto;
import com.vegas.sistema_gestion_operativa.production.domain.entity.Ingredient;
import com.vegas.sistema_gestion_operativa.production.domain.entity.Recipe;
import org.springframework.stereotype.Component;

@Component
public class ProductionFactory {

    public Recipe createFromDto(CreateRecipeDto dto){
        var ingredients = dto.ingredients()
                .stream()
                .map(this::createFromDto)
                .toList();

        return Recipe.builder()
                .productId(dto.productId())
                .unitsProduced(dto.unitsProduced())
                .ingredients(ingredients)
                .observations(dto.observations())
                .active(true)
                .build();
    }

    private Ingredient createFromDto(CreateIngredientDto dto){
        return Ingredient.builder()
                .quantity(new Quantity(dto.quantity()))
                .rawMaterialId(dto.rawMaterialId())
                .observations(dto.observations())
                .build();
    }
}
