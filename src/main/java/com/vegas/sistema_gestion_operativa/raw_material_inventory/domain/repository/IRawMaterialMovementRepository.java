package com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.repository;

import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.entity.RawMaterialMovement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRawMaterialMovementRepository extends JpaRepository<RawMaterialMovement, Long> {
}
