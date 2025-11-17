package com.vegas.sistema_gestion_operativa.production.application.service;

import com.vegas.sistema_gestion_operativa.production.application.dto.CreateRecipeDto;
import com.vegas.sistema_gestion_operativa.production.application.dto.ProductWithRecipeDto;
import com.vegas.sistema_gestion_operativa.production.application.factory.ProductionFactory;
import com.vegas.sistema_gestion_operativa.production.domain.entity.Recipe;
import com.vegas.sistema_gestion_operativa.production.domain.repository.IRecipeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductionService {


    private final IRecipeRepository recipeRepository;
    private final ProductionFactory productionFactory;

    public ProductionService(IRecipeRepository recipeRepository, ProductionFactory productionFactory) {
        this.recipeRepository = recipeRepository;
        this.productionFactory = productionFactory;
    }

    public Recipe createRecipe(CreateRecipeDto dto){
        var newRecipe = productionFactory.createFromDto(dto);
        return recipeRepository.save(newRecipe);
    }

    public Page<ProductWithRecipeDto> getProductsWithRecipes(Pageable pageable) {
        return this.recipeRepository.getProductsWithRecipes(pageable);
    }
}
