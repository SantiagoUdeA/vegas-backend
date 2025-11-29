package com.vegas.sistema_gestion_operativa.reports.infrastructure.repository;

import com.vegas.sistema_gestion_operativa.products_inventory.domain.exceptions.NoMovementsForReportGenerationException;
import com.vegas.sistema_gestion_operativa.reports.domain.entity.MovementItem;
import com.vegas.sistema_gestion_operativa.reports.domain.entity.MovementReport;
import com.vegas.sistema_gestion_operativa.reports.domain.repository.IReportsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ReportsRepositoryJpa implements IReportsRepository {

    private static final String PARAM_BRANCH_ID = "branchId";
    private static final String PARAM_FROM_DATE = "fromDate";
    private static final String PARAM_TO_DATE = "toDate";
    private static final String PARAM_CATEGORY_ID = "categoryId";
    private static final String PARAM_USER_ID = "userId";

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<MovementItem> findProductMovements(
            Long branchId,
            Long categoryId,
            LocalDateTime fromDate,
            LocalDateTime toDate
    ) {
        String jpql = """
                SELECT new com.vegas.sistema_gestion_operativa.reports.domain.entity.MovementItem(
                    p.name,
                    p.category.name,
                    CONCAT(u.givenName, ' ', u.familyName),
                    pim.quantity,
                    pim.movementReason,
                    pim.justification,
                    pim.movementDate,
                    null
                )
                FROM ProductInventoryMovement pim
                JOIN Product p ON pim.productId = p.id
                JOIN User u ON pim.userId = u.id
                WHERE p.branchId = :branchId
                  AND pim.movementDate BETWEEN :fromDate AND :toDate
                  AND (:categoryId IS NULL OR p.category.id = :categoryId)
                """;

        TypedQuery<MovementItem> query = em.createQuery(jpql, MovementItem.class);
        setCommonParameters(query, branchId, fromDate, toDate, categoryId);
        return query.getResultList();
    }

    @Override
    public MovementReport createProductMovementsReport(
            Long branchId,
            Long categoryId,
            LocalDateTime fromDate,
            LocalDateTime toDate,
            String userId
    ) throws NoMovementsForReportGenerationException {
        // Obtener los datos del reporte
        String summaryJpql = """
                SELECT new com.vegas.sistema_gestion_operativa.reports.domain.entity.MovementReport(
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

        MovementReport summary = createReportSummary(summaryJpql, branchId, categoryId, fromDate, toDate, userId);
        List<MovementItem> movements = findProductMovements(branchId, categoryId, fromDate, toDate);

        return buildMovementReport(summary, movements, fromDate, toDate);
    }

    @Override
    public List<MovementItem> findRawMaterialMovements(
            Long branchId,
            Long categoryId,
            LocalDateTime fromDate,
            LocalDateTime toDate
    ) {
        String jpql = """
                SELECT new com.vegas.sistema_gestion_operativa.reports.domain.entity.MovementItem(
                    rm.name,
                    COALESCE(rm.category.name, 'Sin categoría'),
                    CONCAT(u.givenName, ' ', u.familyName),
                    rmm.quantity,
                    rmm.movementReason,
                    rmm.justification,
                    rmm.movementDate,
                    rm.unitOfMeasure
                )
                FROM RawMaterialMovement rmm
                JOIN RawMaterial rm ON rmm.rawMaterialId = rm.id
                JOIN User u ON rmm.userId = u.id
                WHERE rm.branchId = :branchId
                  AND rmm.movementDate BETWEEN :fromDate AND :toDate
                  AND (:categoryId IS NULL OR rm.category.id = :categoryId)
                """;

        TypedQuery<MovementItem> query = em.createQuery(jpql, MovementItem.class);
        setCommonParameters(query, branchId, fromDate, toDate, categoryId);
        return query.getResultList();
    }

    @Override
    public MovementReport createRawMaterialMovementsReport(
            Long branchId,
            Long categoryId,
            LocalDateTime fromDate,
            LocalDateTime toDate,
            String userId
    ) throws NoMovementsForReportGenerationException {
        // Obtener los datos del reporte
        String summaryJpql = """
                SELECT new com.vegas.sistema_gestion_operativa.reports.domain.entity.MovementReport(
                    b.name,
                    CONCAT(u.givenName , ' ', u.familyName),
                    u.roleName,
                    SUM(CASE WHEN rmm.movementReason IN ('ENTRADA','PRODUCCION','COMPRA','RETORNO','PRODUCT_ENTRY') THEN 1 ELSE 0 END),
                    SUM(CASE WHEN rmm.movementReason IN ('SALIDA','AUTOCONSUMO') THEN 1 ELSE 0 END),
                    COUNT(rmm.id),
                    SUM(CASE WHEN rmm.movementReason IN ('AJUSTE_POR_MERMA','AJUSTE_POR_PERDIDA_POR_ROBO','AJUSTE_POR_ERROR_DE_CONTEO') THEN 1 ELSE 0 END)
                )
                FROM RawMaterialMovement rmm
                JOIN RawMaterial rm ON rmm.rawMaterialId = rm.id
                JOIN Branch b ON rm.branchId = b.id
                JOIN User u ON rmm.userId = u.id
                WHERE rm.branchId = :branchId
                  AND rmm.movementDate BETWEEN :fromDate AND :toDate
                  AND rmm.userId = :userId
                  AND (:categoryId IS NULL OR rm.category.id = :categoryId)
                GROUP BY b.name, u.givenName, u.familyName, u.roleName
                """;

        MovementReport summary = createReportSummary(summaryJpql, branchId, categoryId, fromDate, toDate, userId);
        List<MovementItem> movements = findRawMaterialMovements(branchId, categoryId, fromDate, toDate);

        return buildMovementReport(summary, movements, fromDate, toDate);
    }

    /**
     * Establece los parámetros comunes para las consultas de movimientos.
     */
    private <T> void setCommonParameters(
            TypedQuery<T> query,
            Long branchId,
            LocalDateTime fromDate,
            LocalDateTime toDate,
            Long categoryId
    ) {
        query.setParameter(PARAM_BRANCH_ID, branchId)
                .setParameter(PARAM_FROM_DATE, fromDate)
                .setParameter(PARAM_TO_DATE, toDate)
                .setParameter(PARAM_CATEGORY_ID, categoryId);
    }

    /**
     * Crea el resumen del reporte a partir de la consulta JPQL proporcionada.
     */
    private MovementReport createReportSummary(
            String summaryJpql,
            Long branchId,
            Long categoryId,
            LocalDateTime fromDate,
            LocalDateTime toDate,
            String userId
    ) throws NoMovementsForReportGenerationException {
        TypedQuery<MovementReport> query = em.createQuery(summaryJpql, MovementReport.class);
        setCommonParameters(query, branchId, fromDate, toDate, categoryId);
        query.setParameter(PARAM_USER_ID, userId);
        List<MovementReport> results = query.getResultList();
        if (results.isEmpty())
            throw new NoMovementsForReportGenerationException("No se encontraron movimientos para los criterios proporcionados.");
        return results.getFirst();
    }

    /**
     * Construye el reporte de movimientos final con todos los datos.
     */
    private MovementReport buildMovementReport(
            MovementReport summary,
            List<MovementItem> movements,
            LocalDateTime fromDate,
            LocalDateTime toDate
    ) {
        return new MovementReport(
                summary.getBranchName(),
                summary.getUserName(),
                summary.getUserRole(),
                summary.getTotalEntries(),
                summary.getTotalExits(),
                summary.getTotalMovements(),
                summary.getTotalAdjustments(),
                fromDate.toLocalDate(),
                toDate.toLocalDate(),
                movements
        );
    }
}
