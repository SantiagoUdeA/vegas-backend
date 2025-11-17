package com.vegas.sistema_gestion_operativa.production.domain.repository;

import com.vegas.sistema_gestion_operativa.production.domain.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IIngredientsRepository extends JpaRepository<Ingredient,Long> {
}
