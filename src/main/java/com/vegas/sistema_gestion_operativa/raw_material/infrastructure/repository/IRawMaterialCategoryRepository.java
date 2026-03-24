package com.vegas.sistema_gestion_operativa.raw_material.infrastructure.repository;

import com.vegas.sistema_gestion_operativa.raw_material.domain.entity.RawMaterialCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface IRawMaterialCategoryRepository extends JpaRepository<RawMaterialCategory, Long> {
    Optional<RawMaterialCategory> findByNameAndBranchId(String name, Long branchId);
    Page<RawMaterialCategory> findAllByBranchId(Long branchId, Pageable pageable);
}

