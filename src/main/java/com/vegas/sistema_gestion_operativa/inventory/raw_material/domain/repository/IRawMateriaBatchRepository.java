package com.vegas.sistema_gestion_operativa.inventory.raw_material.domain.repository;

import com.vegas.sistema_gestion_operativa.inventory.raw_material.domain.entity.RawMaterialBatch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRawMateriaBatchRepository extends JpaRepository<RawMaterialBatch, Long> {


}

