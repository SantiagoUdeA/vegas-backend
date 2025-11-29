package com.vegas.sistema_gestion_operativa.reports.infrastructure.repository;

import com.vegas.sistema_gestion_operativa.products_inventory.api.ProductInventoryMovementDto;
import com.vegas.sistema_gestion_operativa.reports.application.dto.ProductMovementsReportDto;
import com.vegas.sistema_gestion_operativa.reports.domain.repository.IReportsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ReportsRepositoryJpa implements IReportsRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<ProductInventoryMovementDto> findMovementsForReport(
            Long branchId,
            Long categoryId,
            LocalDateTime fromDate,
            LocalDateTime toDate
    ) {
        String jpql = """
                SELECT new com.vegas.sistema_gestion_operativa.products_inventory.api.ProductInventoryMovementDto(
                    pim.id,
                    p.name,
                    p.category.name,
                    CONCAT(u.givenName, ' ', u.familyName),
                    pim.quantity,
                    pim.movementReason,
                    pim.justification,
                    pim.movementDate
                )
                FROM ProductInventoryMovement pim
                JOIN Product p ON pim.productId = p.id
                JOIN User u ON pim.userId = u.id
                WHERE p.branchId = :branchId
                  AND pim.movementDate BETWEEN :fromDate AND :toDate
                  AND (:categoryId IS NULL OR p.category.id = :categoryId)
                """;

        return em.createQuery(jpql, ProductInventoryMovementDto.class)
                .setParameter("branchId", branchId)
                .setParameter("fromDate", fromDate)
                .setParameter("toDate", toDate)
                .setParameter("categoryId", categoryId)
                .getResultList();
    }

    @Override
    public ProductMovementsReportDto createMovementsReport(
            Long branchId,
            Long categoryId,
            LocalDateTime fromDate,
            LocalDateTime toDate,
            String userId
    ) {
        String jpql = """
                SELECT new com.vegas.sistema_gestion_operativa.reports.application.dto.ProductMovementsReportDto(
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
                """;

        return em.createQuery(jpql, ProductMovementsReportDto.class)
                .setParameter("branchId", branchId)
                .setParameter("fromDate", fromDate)
                .setParameter("toDate", toDate)
                .setParameter("userId", userId)
                .setParameter("categoryId", categoryId)
                .getSingleResult();
    }
}
