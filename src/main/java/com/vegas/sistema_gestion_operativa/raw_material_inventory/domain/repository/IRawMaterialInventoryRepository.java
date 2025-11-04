package com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.repository;

import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.entity.RawMaterialInventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRawMaterialInventoryRepository extends JpaRepository<RawMaterialInventory, Long> {
    RawMaterialInventory findByBranchId(Long branchId);
}
