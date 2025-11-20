package com.vegas.sistema_gestion_operativa.products.domain.repository;

import com.vegas.sistema_gestion_operativa.products.domain.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRecipeRepository extends JpaRepository<Recipe, Long> {

}
