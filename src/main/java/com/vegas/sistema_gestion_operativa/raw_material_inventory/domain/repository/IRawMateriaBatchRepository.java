package com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.repository;

import com.vegas.sistema_gestion_operativa.raw_material_inventory.application.dto.RawMaterialBatchDto;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.entity.RawMaterialBatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IRawMateriaBatchRepository extends JpaRepository<RawMaterialBatch, Long> {

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
}

