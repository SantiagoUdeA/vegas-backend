package com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.repository;

import com.vegas.sistema_gestion_operativa.raw_material_inventory.application.dto.RawMaterialMovementDto;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.entity.RawMaterialMovement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IRawMaterialMovementRepository extends JpaRepository<RawMaterialMovement, Long> {

    @Query("""
                SELECT new com.vegas.sistema_gestion_operativa.raw_material_inventory.application.dto.RawMaterialMovementDto(
                    rmm.id,
                    rmm.quantity,
                    rmm.movementDate,
                    rmm.movementReason,
                    rmb.batchNumber,
                    rm.name,
                    concat(u.givenName, ' ', u.familyName),
                    rmm.justification
                )
                FROM RawMaterialMovement rmm
                JOIN RawMaterial rm ON rmm.rawMaterialId = rm.id
                JOIN User u ON rmm.userId = u.id
                LEFT JOIN RawMaterialBatch rmb ON rmm.rawMaterialBatchId = rmb.id
                WHERE rm.branchId = :branchId
            """)
    Page<RawMaterialMovementDto> findAllByBranchId(Pageable pageable, Long branchId);

    // ── HU13 ────────────────────────────────────────────────────────────────

    /**
     * Paginated movements for a specific batch, ordered chronologically.
     * NOTE: The pageable sort is ignored intentionally — ordering is fixed by the query.
     */
    @Query("""
            SELECT rmm
            FROM RawMaterialMovement rmm
            WHERE rmm.rawMaterialBatchId = :batchId
            ORDER BY rmm.movementDate ASC, rmm.id ASC
            """)
    Page<RawMaterialMovement> findByBatchId(
            @Param("batchId") Long batchId,
            Pageable pageable
    );

    /**
     * All movements of a batch ordered chronologically (no pagination).
     * Used to compute cumulative balanceAfter for every movement.
     */
    @Query("""
            SELECT rmm
            FROM RawMaterialMovement rmm
            WHERE rmm.rawMaterialBatchId = :batchId
            ORDER BY rmm.movementDate ASC, rmm.id ASC
            """)
    List<RawMaterialMovement> findAllByBatchIdOrdered(@Param("batchId") Long batchId);
}

