package com.vegas.sistema_gestion_operativa.production.infrastructure.controller;

import com.vegas.sistema_gestion_operativa.production.application.dto.CreateRecipeDto;
import com.vegas.sistema_gestion_operativa.production.application.service.ProductionService;
import com.vegas.sistema_gestion_operativa.production.domain.entity.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/v1/recipes")
public class RecipeController {

    private final ProductionService productionService;

    @Autowired
    public RecipeController(ProductionService productionService) {
        this.productionService = productionService;
    }

    @PostMapping
    public ResponseEntity<Recipe> createRecipe(@RequestBody CreateRecipeDto dto) {
        return ResponseEntity.ok(productionService.createRecipe(dto));
    }
}
