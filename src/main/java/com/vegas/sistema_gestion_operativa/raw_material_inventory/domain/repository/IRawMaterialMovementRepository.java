package com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.repository;

import com.vegas.sistema_gestion_operativa.raw_material_inventory.application.dto.RawMaterialMovementDto;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.entity.RawMaterialMovement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IRawMaterialMovementRepository extends JpaRepository<RawMaterialMovement, Long> {

    @Query("""
        SELECT new com.vegas.sistema_gestion_operativa.raw_material_inventory.application.dto.RawMaterialMovementDto(
            rmm.id,
            rmm.quantity,
            rmm.movementDate,
            rmm.movementReason,
            rmb.batchNumber,
            rm.name,
            concat(u.givenName, ' ', u.familyName)
        )
        FROM RawMaterialMovement rmm
        JOIN RawMaterial rm ON rmm.rawMaterialId = rm.id
        JOIN User u ON rmm.userId = u.id
        LEFT JOIN RawMaterialBatch rmb ON rmm.rawMaterialBatchId = rmb.id
        WHERE rm.branchId = :branchId
    """)
    Page<RawMaterialMovementDto> findAllByBranchId(Pageable pageable, Long branchId);
}
