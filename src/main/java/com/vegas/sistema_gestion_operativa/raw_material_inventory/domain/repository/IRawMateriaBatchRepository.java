package com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.repository;

import com.vegas.sistema_gestion_operativa.raw_material_inventory.application.dto.BatchDetailDto;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.application.dto.BatchSearchResultDto;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.application.dto.RawMaterialBatchDto;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.entity.RawMaterialBatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IRawMateriaBatchRepository extends JpaRepository<RawMaterialBatch, Long> {

    @Query("""
            SELECT b FROM RawMaterialBatch b
            WHERE b.rawMaterialId = :rawMaterialId
              AND b.branchId = :branchId
              AND b.availableQuantity.value > 0
            ORDER BY b.entryDate ASC, b.id ASC
            """)
    List<RawMaterialBatch> findAvailableBatchesFifo(
            @Param("rawMaterialId") Long rawMaterialId,
            @Param("branchId") Long branchId
    );

    @Query("""
            SELECT COALESCE(SUM(b.availableQuantity.value), 0)
            FROM RawMaterialBatch b
            WHERE b.rawMaterialId = :rawMaterialId
              AND b.branchId = :branchId
              AND b.availableQuantity.value > 0
            """)
    BigDecimal sumAvailableByRawMaterialAndBranch(
            @Param("rawMaterialId") Long rawMaterialId,
            @Param("branchId") Long branchId
    );

    @Query("""
            SELECT new com.vegas.sistema_gestion_operativa.raw_material_inventory.application.dto.RawMaterialBatchDto(
                rmb.id,
                rmb.batchNumber,
                rmb.quantity,
                rm.unitOfMeasure,
                rmb.totalCost,
                rmb.entryDate,
                rmb.expirationDate,
                rmb.branchId,
                rmb.rawMaterialId,
                rmb.providerId
            )
            FROM RawMaterialBatch rmb
            JOIN com.vegas.sistema_gestion_operativa.raw_material.domain.entity.RawMaterial rm ON rmb.rawMaterialId = rm.id
            WHERE rm.branchId = :branchId
            """)
    Page<RawMaterialBatchDto> findByBranchId(@Param("branchId") Long branchId, Pageable pageable);

    // ── HU13 ────────────────────────────────────────────────────────────────

    /**
     * Returns full batch detail with resolved names (raw material + provider).
     * Used by the batch history endpoint.
     */
    @Query("""
            SELECT new com.vegas.sistema_gestion_operativa.raw_material_inventory.application.dto.BatchDetailDto(
                b.id,
                b.batchNumber,
                b.rawMaterialId,
                rm.name,
                rm.unitOfMeasure,
                b.quantity,
                b.availableQuantity,
                b.totalCost,
                b.entryDate,
                b.expirationDate,
                b.providerId,
                p.name,
                b.branchId
            )
            FROM RawMaterialBatch b
            JOIN com.vegas.sistema_gestion_operativa.raw_material.domain.entity.RawMaterial rm ON b.rawMaterialId = rm.id
            JOIN com.vegas.sistema_gestion_operativa.provider.domain.entity.Provider p ON b.providerId = p.id
            WHERE b.id = :batchId
            """)
    Optional<BatchDetailDto> findBatchDetailById(@Param("batchId") Long batchId);

    /**
     * Case-insensitive partial search by batchNumber scoped to a branch.
     * Used by the batch search endpoint.
     */
    @Query("""
            SELECT new com.vegas.sistema_gestion_operativa.raw_material_inventory.application.dto.BatchSearchResultDto(
                b.id,
                b.batchNumber,
                rm.name,
                rm.unitOfMeasure,
                b.quantity,
                b.availableQuantity,
                b.entryDate,
                b.expirationDate,
                p.name
            )
            FROM RawMaterialBatch b
            JOIN com.vegas.sistema_gestion_operativa.raw_material.domain.entity.RawMaterial rm ON b.rawMaterialId = rm.id
            JOIN com.vegas.sistema_gestion_operativa.provider.domain.entity.Provider p ON b.providerId = p.id
            WHERE b.branchId = :branchId
              AND LOWER(b.batchNumber) LIKE LOWER(CONCAT('%', :query, '%'))
            ORDER BY b.entryDate DESC
            """)
    Page<BatchSearchResultDto> searchByBatchNumber(
            @Param("branchId") Long branchId,
            @Param("query") String query,
            Pageable pageable
    );
}
