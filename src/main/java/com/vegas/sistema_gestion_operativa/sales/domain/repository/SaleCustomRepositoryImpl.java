package com.vegas.sistema_gestion_operativa.sales.domain.repository;

import com.vegas.sistema_gestion_operativa.sales.application.dto.DailySalesAmountDto;
import com.vegas.sistema_gestion_operativa.sales.application.dto.ProductSalesStatsDto;
import com.vegas.sistema_gestion_operativa.sales.domain.entity.Sale;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SaleCustomRepositoryImpl implements SaleCustomRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<Sale> findAllFiltered(Long branchId, LocalDate from, LocalDate to, Pageable pageable) {

        StringBuilder sb = new StringBuilder(
                "SELECT DISTINCT s FROM Sale s LEFT JOIN FETCH s.details d WHERE 1=1 "
        );

        if (branchId != null) {
            sb.append(" AND s.branchId = :branchId ");
        }

        if (from != null) {
            sb.append(" AND s.saleDate >= :from ");
        }

        if (to != null) {
            sb.append(" AND s.saleDate <= :to ");
        }

        // Query para datos
        TypedQuery<Sale> query = em.createQuery(sb.toString(), Sale.class);

        // Query para contar
        String countQuery = sb.toString().replace(
                "SELECT DISTINCT s FROM Sale s LEFT JOIN FETCH s.details d",
                "SELECT COUNT(s) FROM Sale s"
        );
        TypedQuery<Long> count = em.createQuery(countQuery, Long.class);

        // Params
        if (branchId != null) {
            query.setParameter("branchId", branchId);
            count.setParameter("branchId", branchId);
        }

        if (from != null) {
            query.setParameter("from", from.atStartOfDay());
            count.setParameter("from", from.atStartOfDay());
        }

        if (to != null) {
            query.setParameter("to", to.atTime(23, 59, 59));
            count.setParameter("to", to.atTime(23, 59, 59));
        }

        // Paginación
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Sale> resultList = query.getResultList();
        long total = count.getSingleResult();

        return new PageImpl<>(resultList, pageable, total);
    }

    private List<ProductSalesStatsDto> getProductSalesStatsDto(Long branchId, LocalDate from, LocalDate to, int limit, StringBuilder sb) {
        var query = em.createQuery(sb.toString(), ProductSalesStatsDto.class);

        if (branchId != null) query.setParameter("branchId", branchId);
        if (from != null) query.setParameter("from", from.atStartOfDay());
        if (to != null) query.setParameter("to", to.atTime(23, 59, 59));

        query.setMaxResults(limit);

        return query.getResultList();
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ProductSalesStatsDto> findAllProductsSalesStats(Long branchId, LocalDate from, LocalDate to) {

        StringBuilder sql = new StringBuilder();
        sql.append("""
            SELECT 
                d.product_id AS productId,
                p.name AS productName,
                SUM(d.quantity) AS totalQuantitySold
            FROM sale s
            JOIN sale_detail d ON s.sale_id = d.sale_id
            JOIN product p ON p.id = d.product_id
            WHERE 1=1
        """);

        if (branchId != null) {
            sql.append(" AND s.branch_id = :branchId");
        }
        if (from != null) {
            sql.append(" AND s.sale_date >= :from");
        }
        if (to != null) {
            sql.append(" AND s.sale_date <= :to");
        }

        sql.append(" GROUP BY d.product_id, p.name");
        sql.append(" ORDER BY totalQuantitySold DESC");

        Query query = entityManager.createNativeQuery(sql.toString());

        if (branchId != null) query.setParameter("branchId", branchId);
        if (from != null) query.setParameter("from", from.atStartOfDay());
        if (to != null) query.setParameter("to", to.atTime(23,59,59));

        List<Object[]> results = query.getResultList();

        List<ProductSalesStatsDto> list = new ArrayList<>();

        for (Object[] row : results) {
            list.add(new ProductSalesStatsDto(
                    ((Number) row[0]).longValue(),          // productId
                    (String) row[1],                        // productName
                    new BigDecimal(row[2].toString())       // totalQuantitySold
            ));
        }

        return list;
    }

    @Override
    public List<DailySalesAmountDto> findDailySales(Long branchId, LocalDate from, LocalDate to) {

        // Rango por defecto: último mes
        if (to == null) to = LocalDate.now();
        if (from == null) from = to.minusDays(30);

        StringBuilder sql = new StringBuilder("""
        SELECT 
            DATE(s.sale_date) AS saleDay,
            SUM(s.total) AS totalAmount
        FROM sale s
        WHERE s.sale_date BETWEEN :from AND :to
    """);

        if (branchId != null) {
            sql.append(" AND s.branch_id = :branchId ");
        }

        sql.append(" GROUP BY saleDay ORDER BY saleDay ASC ");

        Query query = entityManager.createNativeQuery(sql.toString());

        query.setParameter("from", from.atStartOfDay());
        query.setParameter("to", to.atTime(23, 59, 59));

        if (branchId != null) {
            query.setParameter("branchId", branchId);
        }

        List<Object[]> rows = query.getResultList();
        List<DailySalesAmountDto> result = new ArrayList<>();

        for (Object[] row : rows) {
            result.add(new DailySalesAmountDto(
                    ((java.sql.Date) row[0]).toLocalDate(),
                    new BigDecimal(row[1].toString())
            ));
        }

        return result;
    }
}
