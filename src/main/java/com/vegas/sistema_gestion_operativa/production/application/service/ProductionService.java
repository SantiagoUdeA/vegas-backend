package com.vegas.sistema_gestion_operativa.production.application.service;

import com.vegas.sistema_gestion_operativa.production.application.dto.CreateRecipeDto;
import com.vegas.sistema_gestion_operativa.production.application.factory.ProductionFactory;
import com.vegas.sistema_gestion_operativa.production.domain.entity.Recipe;
import com.vegas.sistema_gestion_operativa.production.domain.repository.IIngredientsRepository;
import com.vegas.sistema_gestion_operativa.production.domain.repository.IRecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductionService {

    private final IRecipeRepository recipeRepository;
    private final IIngredientsRepository ingredientsRepository;
    private final ProductionFactory productionFactory;

    public ProductionService(IRecipeRepository recipeRepository, IIngredientsRepository ingredientsRepository, ProductionFactory productionFactory) {
        this.recipeRepository = recipeRepository;
        this.ingredientsRepository = ingredientsRepository;
        this.productionFactory = productionFactory;
    }

    @Transactional
    public Recipe createRecipe(CreateRecipeDto dto){
        var recipe = recipeRepository.save(productionFactory.createFromDto(dto));
        var ingredients = productionFactory.createIngredientsFromDto(dto.ingredients(), recipe.getId());
        ingredientsRepository.saveAll(ingredients);
        recipe.setIngredients(ingredients);
        return recipe;
    }
}
