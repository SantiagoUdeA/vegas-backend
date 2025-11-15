package com.vegas.sistema_gestion_operativa.catalog.raw_material.infrastructure.repository;

import com.vegas.sistema_gestion_operativa.catalog.raw_material.domain.entity.RawMaterial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRawMaterialRepository extends JpaRepository<RawMaterial, Long> {

    Page<RawMaterial> findByActiveTrue(Pageable pageable);

    Optional<RawMaterial> findByNameAndActiveTrue(String nameActive);

    Page<RawMaterial> findByActiveTrueAndBranchId(Long branchId, Pageable pageable);

    Optional<RawMaterial> findByNameAndActiveTrueAndBranchId(String name, Long branchId);
}
