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

    // 🔍 Obtener stock actual sin exponer la entidad
    @Query(value = """
                SELECT COALESCE(pi.current_stock, 0)
                FROM product_inventory pi
                WHERE pi.product_id = :productId
            """, nativeQuery = true)
    Double findCurrentStockByProductId(@Param("productId") Long productId);

    // Listado de inventario por sede
    @Query(value = """
                SELECT
                    pi.id AS id,
                    p.id AS productId,
                    p.name AS productName,
                    pc.name AS categoryName,
                    pi.current_stock AS currentStock,
                    COALESCE(
                        (
                            SELECT SUM(i.quantity * rmi.average_cost) / MAX(r.units_produced)
                            FROM recipe r
                            JOIN ingredient i ON i.recipe_id = r.id
                            JOIN raw_material_inventory rmi ON i.raw_material_id = rmi.raw_material_id
                            WHERE r.product_id = p.id
                              AND r.active = true
                              AND rmi.branch_id = :branchId
                        ),
                        0.0
                    ) AS averageCost,
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
    Page<ProductInventoryItemDto> findInventoryItemsByBranchId(
            @Param("branchId") Long branchId,
            Pageable pageable
    );

}
