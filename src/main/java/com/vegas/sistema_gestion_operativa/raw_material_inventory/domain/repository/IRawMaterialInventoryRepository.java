package com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.repository;

import com.vegas.sistema_gestion_operativa.raw_material_inventory.application.dto.RawMaterialInventoryItemDto;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.application.dto.RawMaterialValuationItemDto;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.entity.RawMaterialInventory;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IRawMaterialInventoryRepository extends JpaRepository<RawMaterialInventory, Long> {

    List<RawMaterialInventory> findAllByBranchId(Long branchId);

    // 🟦 EXISTENTE — NO SE TOCA
    @Query(value = """
        SELECT
            rmi.id AS id,
            rm.name AS rawMaterialName,
            rmc.name AS categoryName,
            rm.unit_of_measure AS unitOfMeasure,
            rmi.current_stock AS currentStock,
            rmi.average_cost AS averageCost,
            rmi.updated_at AS updatedAt
        FROM raw_material_inventory rmi
        JOIN raw_material rm ON rmi.raw_material_id = rm.id
        JOIN raw_material_category rmc ON rm.category_id = rmc.id
        WHERE rmi.branch_id = :branchId
    """, nativeQuery = true)
    List<RawMaterialInventoryItemDto> findInventoryItemsByBranchId(@Param("branchId") Long branchId);


    // 🟩 NUEVO — PARA VALUACIÓN
    @Query(value = """
        SELECT
            rm.id AS rawMaterialId,
            rm.name AS rawMaterialName,
            rmc.name AS categoryName,
            COALESCE(rmi.current_stock, 0) AS currentStock,
            COALESCE(rmi.average_cost, 0) AS averageCost,
            (COALESCE(rmi.current_stock, 0) * COALESCE(rmi.average_cost, 0)) AS valuation
        FROM raw_material_inventory rmi
        JOIN raw_material rm ON rmi.raw_material_id = rm.id
        JOIN raw_material_category rmc ON rm.category_id = rmc.id
        WHERE rmi.branch_id = :branchId
    """, nativeQuery = true)
    List<RawMaterialValuationItemDto> findValuationItemsByBranchId(@Param("branchId") Long branchId);


    Optional<RawMaterialInventory> findByRawMaterialIdAndBranchId(
            @NotNull Long rawMaterialId,
            @NotNull Long branchId
    );

    List<RawMaterialInventory> findByRawMaterialIdIn(List<Long> rawMaterialIds);
}