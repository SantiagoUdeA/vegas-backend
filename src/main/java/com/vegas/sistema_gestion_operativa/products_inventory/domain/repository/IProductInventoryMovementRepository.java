package com.vegas.sistema_gestion_operativa.products_inventory.domain.repository;

import com.vegas.sistema_gestion_operativa.products_inventory.application.dto.ProductInventoryMovementDto;
import com.vegas.sistema_gestion_operativa.products_inventory.application.dto.ProductMovementsReportDto;
import com.vegas.sistema_gestion_operativa.products_inventory.domain.entity.ProductInventoryMovement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface IProductInventoryMovementRepository extends JpaRepository<ProductInventoryMovement, Long> {
    @Query("""
                SELECT new com.vegas.sistema_gestion_operativa.products_inventory.application.dto.ProductInventoryMovementDto(
                    pim.id,
                    p.name,
                    p.category.name,
                    concat(u.givenName, ' ', u.familyName),
                    pim.quantity,
                    pim.movementReason,
                    pim.justification,
                    pim.movementDate
                 )
                FROM ProductInventoryMovement pim
                JOIN Product p ON pim.productId = p.id
                JOIN User u ON pim.userId = u.id
                WHERE p.branchId = :branchId
            """)
    Page<ProductInventoryMovementDto> findAllByBranchId(Pageable pageable, Long branchId);

    @Query("""
                SELECT new com.vegas.sistema_gestion_operativa.products_inventory.application.dto.ProductInventoryMovementDto(
                    pim.id,
                    p.name,
                    p.category.name,
                    concat(u.givenName, ' ', u.familyName),
                    pim.quantity,
                    pim.movementReason,
                    pim.justification,
                    pim.movementDate
                 )
                FROM ProductInventoryMovement pim
                JOIN Product p ON pim.productId = p.id
                JOIN User u ON pim.userId = u.id
                WHERE (p.branchId = :branchId)
                  AND pim.movementDate BETWEEN :fromDate AND :toDate
                  AND (:categoryId IS NULL OR p.category.id = :categoryId)
            """)
    List<ProductInventoryMovementDto> findMovementsForReport(
            @Param("branchId") Long branchId,
            @Param("categoryId") Long categoryId,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate
    );

    @Query("""
                SELECT new com.vegas.sistema_gestion_operativa.products_inventory.application.dto.ProductMovementsReportDto(
                    b.name,
                    CONCAT(u.givenName , ' ', u.familyName),
                    u.roleName,
                    SUM(CASE WHEN pim.movementReason IN ('ENTRADA','PRODUCCION','COMPRA','RETORNO','PRODUCT_ENTRY') THEN 1 ELSE 0 END),
                    SUM(CASE WHEN pim.movementReason IN ('SALIDA','AUTOCONSUMO') THEN 1 ELSE 0 END),
                    COUNT(pim.id),
                    SUM(CASE WHEN pim.movementReason IN ('AJUSTE_POR_MERMA','AJUSTE_POR_PERDIDA_POR_ROBO','AJUSTE_POR_ERROR_DE_CONTEO') THEN 1 ELSE 0 END)
                )
                FROM ProductInventoryMovement pim
                JOIN Product p ON pim.productId = p.id
                JOIN Branch b ON p.branchId = b.id
                JOIN User u ON pim.userId = u.id
                WHERE p.branchId = :branchId
                  AND pim.movementDate BETWEEN :fromDate AND :toDate
                  AND pim.userId = :userId
                  AND (:categoryId IS NULL OR p.category.id = :categoryId)
                GROUP BY b.name, u.givenName, u.familyName, u.roleName
            """)
    ProductMovementsReportDto createMovementsReport(
            @Param("branchId") Long branchId,
            @Param("categoryId") Long categoryId,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            @Param("userId") String userId
    );

}
