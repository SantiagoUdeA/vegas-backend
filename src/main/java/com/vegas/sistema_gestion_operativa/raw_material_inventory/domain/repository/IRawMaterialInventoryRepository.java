package com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.repository;

import com.vegas.sistema_gestion_operativa.raw_material_inventory.appliaction.dto.RawMaterialInventoryItemDto;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.entity.RawMaterialInventory;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IRawMaterialInventoryRepository extends JpaRepository<RawMaterialInventory, Long> {

    List<RawMaterialInventory> findAllByBranchId(Long branchId);

    @Query(value = """
    SELECT\s
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

    Optional<RawMaterialInventory> findByRawMaterialIdAndBranchId(@NotNull(message = "El ID del material prima no puede ser nulo") Long rawMaterialId, @NotNull(message = "El ID de la sede no puede ser nulo") Long branchId);

    List<RawMaterialInventory> findByRawMaterialIdIn(List<Long> rawMaterialIds);
}
