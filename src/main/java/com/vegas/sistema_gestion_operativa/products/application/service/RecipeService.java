package com.vegas.sistema_gestion_operativa.products.application.service;

import com.vegas.sistema_gestion_operativa.products.application.dto.CreateRecipeDto;
import com.vegas.sistema_gestion_operativa.products.application.factory.RecipeFactory;
import com.vegas.sistema_gestion_operativa.products.domain.entity.Recipe;
import com.vegas.sistema_gestion_operativa.products.domain.repository.IIngredientsRepository;
import com.vegas.sistema_gestion_operativa.products.domain.repository.IRecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecipeService {

    private final IRecipeRepository recipeRepository;
    private final IIngredientsRepository ingredientsRepository;
    private final RecipeFactory recipeFactory;

    public RecipeService(IRecipeRepository recipeRepository, IIngredientsRepository ingredientsRepository, RecipeFactory recipeFactory) {
        this.recipeRepository = recipeRepository;
        this.ingredientsRepository = ingredientsRepository;
        this.recipeFactory = recipeFactory;
    }

    @Transactional
    public Recipe createRecipe(CreateRecipeDto dto){
        var receta = recipeFactory.createFromDto(dto);
        var recipe = recipeRepository.save(receta);
        var ingredients = recipeFactory.createIngredientsFromDto(dto.ingredients(), recipe.getId());
        ingredientsRepository.saveAll(ingredients);
        recipe.setIngredients(ingredients);
        return recipe;
    }
}
