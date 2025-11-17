package com.vegas.sistema_gestion_operativa.production.domain.repository;

import com.vegas.sistema_gestion_operativa.production.application.dto.ProductWithRecipeDto;
import com.vegas.sistema_gestion_operativa.production.domain.entity.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IRecipeRepository extends JpaRepository<Recipe, Long> {

    @Query("""
            SELECT p.id as productId, p.name as productName, p.active as productActive,
                   r.id as recipeId, r.unitsProduced, r.active as recipeActive, r.observations
            FROM Product p
            INNER JOIN Recipe r ON r.productId = p.id
            WHERE p.active = true AND r.active = true
            """)
    Page<ProductWithRecipeDto> getProductsWithRecipes(Pageable pageable);
}
