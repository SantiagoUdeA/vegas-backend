package com.vegas.sistema_gestion_operativa.products.infrastructure.controller;

import com.vegas.sistema_gestion_operativa.common.exceptions.AccessDeniedException;
import com.vegas.sistema_gestion_operativa.products.api.RecipeDto;
import com.vegas.sistema_gestion_operativa.products.application.dto.CreateRecipeDto;
import com.vegas.sistema_gestion_operativa.products.application.dto.UpdateRecipeDto;
import com.vegas.sistema_gestion_operativa.products.application.service.RecipeService;
import com.vegas.sistema_gestion_operativa.products.domain.entity.Recipe;
import com.vegas.sistema_gestion_operativa.products.domain.exceptions.RecipeNotFoundException;
import com.vegas.sistema_gestion_operativa.security.AuthUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("api/v1/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping
    public ResponseEntity<Recipe> createRecipe(@RequestBody @Valid CreateRecipeDto dto) {
        return ResponseEntity.ok(recipeService.createRecipe(dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RecipeDto> updateRecipe(@PathVariable Long id, @RequestBody @Valid UpdateRecipeDto dto) throws RecipeNotFoundException, AccessDeniedException {
        return ResponseEntity.ok(recipeService.updateRecipe(AuthUtils.getUserIdFromToken(), id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RecipeDto> deleteRecipe(@PathVariable Long id) throws RecipeNotFoundException, AccessDeniedException {
        return ResponseEntity.ok(recipeService.deleteRecipe(AuthUtils.getUserIdFromToken(), id));
    }
}
