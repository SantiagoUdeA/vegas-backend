package com.vegas.sistema_gestion_operativa.inventory.raw_material.domain.repository;

import com.vegas.sistema_gestion_operativa.inventory.raw_material.domain.entity.RawMaterialMovement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRawMaterialMovementRepository extends JpaRepository<RawMaterialMovement, Long> {
}
