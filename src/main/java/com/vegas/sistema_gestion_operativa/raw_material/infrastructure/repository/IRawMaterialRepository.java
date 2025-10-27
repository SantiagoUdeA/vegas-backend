package com.vegas.sistema_gestion_operativa.raw_material.infrastructure.repository;

import com.vegas.sistema_gestion_operativa.raw_material.domain.entity.RawMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IRawMaterialRepository extends JpaRepository<RawMaterial, Long> {

    List<RawMaterial> findByActiveTrue();
}
