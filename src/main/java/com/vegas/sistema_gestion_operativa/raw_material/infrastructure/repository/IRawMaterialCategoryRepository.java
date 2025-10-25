package com.vegas.sistema_gestion_operativa.raw_material.infrastructure.repository;

import com.vegas.sistema_gestion_operativa.raw_material.domain.entity.RawMaterialCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRawMaterialCategoryRepository extends JpaRepository<RawMaterialCategory, Long> {
}

