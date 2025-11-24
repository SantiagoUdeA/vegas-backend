package com.vegas.sistema_gestion_operativa.products.application.factory;

import com.vegas.sistema_gestion_operativa.common.domain.Quantity;
import com.vegas.sistema_gestion_operativa.products.application.dto.CreateIngredientDto;
import com.vegas.sistema_gestion_operativa.products.application.dto.CreateRecipeDto;
import com.vegas.sistema_gestion_operativa.products.application.dto.UpdateIngredientDto;
import com.vegas.sistema_gestion_operativa.products.domain.entity.Ingredient;
import com.vegas.sistema_gestion_operativa.products.domain.entity.Recipe;
import com.vegas.sistema_gestion_operativa.products.domain.entity.Product;
import com.vegas.sistema_gestion_operativa.raw_material.domain.entity.RawMaterial;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecipeFactory {

    public Recipe createFromDto(CreateRecipeDto dto){
        return Recipe.builder()
                .product(Product.builder().id(dto.productId()).build())
                .unitsProduced(dto.unitsProduced())
                .observations(dto.observations())
                .active(true)
                .build();
    }

    public List<Ingredient> createIngredientsFromDto(List<CreateIngredientDto> dto, Long recipeId){
        return dto.stream().map(ingredientDto ->
            Ingredient.builder()
                .recipe(Recipe.builder().id(recipeId).build())
                .rawMaterial(RawMaterial.builder().id(ingredientDto.rawMaterialId()).build())
                .quantity(new Quantity(ingredientDto.quantity()))
                .observations(ingredientDto.observations())
                .build()
        ).toList();
    }

    public List<Ingredient> createIngredientsFroUpdateDto(List<UpdateIngredientDto> dto, Long recipeId){
        return dto.stream().map(ingredientDto ->
                Ingredient.builder()
                        .recipe(Recipe.builder().id(recipeId).build())
                        .rawMaterial(RawMaterial.builder().id(ingredientDto.rawMaterialId()).build())
                        .quantity(new Quantity(ingredientDto.quantity()))
                        .observations(ingredientDto.observations())
                        .build()
        ).toList();
    }

}
