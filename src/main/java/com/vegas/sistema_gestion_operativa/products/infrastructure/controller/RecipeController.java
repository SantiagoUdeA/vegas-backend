package com.vegas.sistema_gestion_operativa.products.infrastructure.controller;

import com.vegas.sistema_gestion_operativa.products.application.dto.CreateRecipeDto;
import com.vegas.sistema_gestion_operativa.products.application.service.RecipeService;
import com.vegas.sistema_gestion_operativa.products.domain.entity.Recipe;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
