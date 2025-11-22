package com.vegas.sistema_gestion_operativa.products_inventory.domain.repository;

import com.vegas.sistema_gestion_operativa.products_inventory.application.dto.ProductInventoryItemDto;
import com.vegas.sistema_gestion_operativa.products_inventory.domain.entity.ProductInventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IProductInventoryRepository extends JpaRepository<ProductInventory, Long> {

    Optional<ProductInventory> findByProductId(Long productId);

    /*
        Se suma el costo total de todos los ingredientes (cantidad × costo de cada uno)
        y luego se divide entre unitsProduced

        Si una receta usa 10 kg de harina que cuesta 5 $/kg = 50 $
        Y usa 2 kg de azúcar que cuesta 3 $/kg = 6 $ total
        Costo total de materias primas = $56
        Si la receta produce 20 unidades, entonces el costo por unidad = 56 $ / 20 = 2.80$
     */
    @Query(
            """
            SELECT COALESCE(SUM(i.quantity.value * rmi.averageCost.value) / MAX(r.unitsProduced), 0.0)
            FROM Ingredient i
            JOIN Recipe r ON i.recipe.id = r.id
            JOIN RawMaterialInventory rmi ON i.rawMaterialId = rmi.rawMaterialId
            WHERE r.product.id = :productId
            AND r.active = true
            """
    )
    Double calculateAverageProductCost(@Param("productId") Long productId);

    @Query(value = """
        SELECT
            p.name AS productName,
            pc.name AS categoryName,
            pi.current_stock AS currentStock,
            pi.average_cost AS averageCost,
            pi.updated_at AS updatedAt
        FROM product_inventory pi
        JOIN product p ON pi.product_id = p.id
        LEFT JOIN product_category pc ON p.category_id = pc.id
        WHERE pi.branch_id = :branchId
    """,
    countQuery = """
        SELECT COUNT(*)
        FROM product_inventory pi
        WHERE pi.branch_id = :branchId
    """,
    nativeQuery = true)
    Page<ProductInventoryItemDto> findInventoryItemsByBranchId(@Param("branchId") Long branchId, Pageable pageable);

}

