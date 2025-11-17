package com.vegas.sistema_gestion_operativa.production.application.factory;

import com.vegas.sistema_gestion_operativa.common.domain.Quantity;
import com.vegas.sistema_gestion_operativa.production.application.dto.CreateIngredientDto;
import com.vegas.sistema_gestion_operativa.production.application.dto.CreateRecipeDto;
import com.vegas.sistema_gestion_operativa.production.domain.entity.Ingredient;
import com.vegas.sistema_gestion_operativa.production.domain.entity.Recipe;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductionFactory {

    public Recipe createFromDto(CreateRecipeDto dto){
        return Recipe.builder()
                .productId(dto.productId())
                .unitsProduced(dto.unitsProduced())
                .observations(dto.observations())
                .active(true)
                .build();
    }

    public List<Ingredient> createIngredientsFromDto(List<CreateIngredientDto> dto, Long recipeId){
        return dto.stream().map(ingredientDto ->
            Ingredient.builder()
                .recipeId(recipeId)
                .rawMaterialId(ingredientDto.rawMaterialId())
                .quantity(new Quantity(ingredientDto.quantity()))
                .observations(ingredientDto.observations())
                .build()
        ).toList();
    }
}
