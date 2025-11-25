package com.vegas.sistema_gestion_operativa.products.application.service;

import com.vegas.sistema_gestion_operativa.branches.IBranchApi;
import com.vegas.sistema_gestion_operativa.common.exceptions.AccessDeniedException;
import com.vegas.sistema_gestion_operativa.products.api.RecipeDto;
import com.vegas.sistema_gestion_operativa.products.application.dto.CreateRecipeDto;
import com.vegas.sistema_gestion_operativa.products.application.dto.UpdateRecipeDto;
import com.vegas.sistema_gestion_operativa.products.application.factory.RecipeFactory;
import com.vegas.sistema_gestion_operativa.products.application.mapper.IProductMapper;
import com.vegas.sistema_gestion_operativa.products.domain.entity.Ingredient;
import com.vegas.sistema_gestion_operativa.products.domain.entity.Recipe;
import com.vegas.sistema_gestion_operativa.products.domain.exceptions.RecipeNotFoundException;
import com.vegas.sistema_gestion_operativa.products.domain.repository.IIngredientsRepository;
import com.vegas.sistema_gestion_operativa.products.domain.repository.IProductRepository;
import com.vegas.sistema_gestion_operativa.products.domain.repository.IRecipeRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final IRecipeRepository recipeRepository;
    private final IIngredientsRepository ingredientsRepository;
    private final IProductRepository productRepository;
    private final RecipeFactory recipeFactory;
    private final IProductMapper productMapper;
    private final IBranchApi branchApi;

    @Transactional
    public Recipe createRecipe(CreateRecipeDto dto) {
        var receta = recipeFactory.createFromDto(dto);
        var recipe = recipeRepository.save(receta);
        var ingredients = recipeFactory.createIngredientsFromDto(dto.ingredients(), recipe.getId());
        ingredientsRepository.saveAll(ingredients);
        recipe.setIngredients(ingredients);
        return recipe;
    }

    @Transactional
    public RecipeDto updateRecipe(String userId, Long recipeId, @Valid UpdateRecipeDto dto) throws RecipeNotFoundException, AccessDeniedException {
        var recipe = retrieveRecipeOrThrow(recipeId);

        branchApi.assertUserHasAccessToBranch(userId, recipe.getProduct().getBranchId());

        if (dto.unitsProduced() != null) {
            recipe.setUnitsProduced(dto.unitsProduced());
        }

        if (dto.unitsProduced() != null) {
            recipe.setUnitsProduced(dto.unitsProduced());
        }

        if (dto.ingredients() != null) {
            // Eliminar ingredientes existentes
            ingredientsRepository.deleteAll(recipe.getIngredients());
            recipe.getIngredients().clear();

            // Crear y guardar nuevos ingredientes
            List<Ingredient> newIngredients = recipeFactory.createIngredientsFroUpdateDto(dto.ingredients(), recipe.getId());
            ingredientsRepository.saveAll(newIngredients);

            // Agregar nuevos ingredientes a la colección existente
            // Debe ser asi para que no de error java.lang.UnsupportedOperationException
            recipe.getIngredients().addAll(newIngredients);
        }

        return productMapper.toRecipeDto(
                recipeRepository.save(recipe)
        );
    }

    @Transactional
    public RecipeDto deleteRecipe(String userId, Long recipeId) throws RecipeNotFoundException, AccessDeniedException {
        var recipe = retrieveRecipeOrThrow(recipeId);

        branchApi.assertUserHasAccessToBranch(userId, recipe.getProduct().getBranchId());

        RecipeDto dto = productMapper.toRecipeDto(recipe);

        // Romper la relación bidireccional
        var product = recipe.getProduct();
        if (product != null) {
            product.setRecipe(null);
            recipe.setProduct(null);
            productRepository.save(product);
            recipeRepository.save(recipe);
        }

        return dto;
    }

    public Recipe retrieveRecipeOrThrow(Long id) throws RecipeNotFoundException {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException(id));
    }
}
