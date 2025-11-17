package com.vegas.sistema_gestion_operativa.production.infrastructure.controller;

import com.vegas.sistema_gestion_operativa.common.dto.PageResponse;
import com.vegas.sistema_gestion_operativa.common.dto.PaginationRequest;
import com.vegas.sistema_gestion_operativa.common.utils.PaginationUtils;
import com.vegas.sistema_gestion_operativa.production.application.dto.CreateRecipeDto;
import com.vegas.sistema_gestion_operativa.production.application.dto.ProductWithRecipeDto;
import com.vegas.sistema_gestion_operativa.production.application.service.ProductionService;
import com.vegas.sistema_gestion_operativa.production.domain.entity.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping
    public ResponseEntity<PageResponse<ProductWithRecipeDto>> getProductsWithRecipe(PaginationRequest paginationRequest) {
        Pageable pageable = PaginationUtils.getPageable(paginationRequest);
        Page<ProductWithRecipeDto> page = productionService.getProductsWithRecipes(pageable);
        return ResponseEntity.ok(PageResponse.from(page));
    }
}
