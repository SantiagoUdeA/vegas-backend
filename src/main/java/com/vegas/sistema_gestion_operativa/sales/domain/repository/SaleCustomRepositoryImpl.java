package com.vegas.sistema_gestion_operativa.sales.domain.repository;

import com.vegas.sistema_gestion_operativa.sales.application.dto.ProductSalesStatsDto;
import com.vegas.sistema_gestion_operativa.sales.domain.entity.Sale;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SaleCustomRepositoryImpl implements SaleCustomRepository {

    private final EntityManager em;

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

    @Override
    public List<ProductSalesStatsDto> findTopSellingProducts(Long branchId, LocalDate from, LocalDate to, int limit) {

        StringBuilder sb = new StringBuilder(
                "SELECT new com.vegas.sistema_gestion_operativa.sales.application.dto.ProductSalesStatsDto(" +
                        "d.productId, p.name, SUM(d.quantity.value)) " +
                        "FROM SaleDetail d " +
                        "JOIN d.sale s " +
                        "JOIN Product p ON p.id = d.productId " +
                        "WHERE 1 = 1 "
        );

        if (branchId != null) sb.append(" AND s.branchId = :branchId ");
        if (from != null) sb.append(" AND s.saleDate >= :from ");
        if (to != null) sb.append(" AND s.saleDate <= :to ");

        sb.append(" GROUP BY d.productId, p.name ORDER BY SUM(d.quantity.value) DESC");

        return getProductSalesStatsDto(branchId, from, to, limit, sb);
    }

    private List<ProductSalesStatsDto> getProductSalesStatsDto(Long branchId, LocalDate from, LocalDate to, int limit, StringBuilder sb) {
        var query = em.createQuery(sb.toString(), ProductSalesStatsDto.class);

        if (branchId != null) query.setParameter("branchId", branchId);
        if (from != null) query.setParameter("from", from.atStartOfDay());
        if (to != null) query.setParameter("to", to.atTime(23, 59, 59));

        query.setMaxResults(limit);

        return query.getResultList();
    }

    @Override
    public List<ProductSalesStatsDto> findLeastSellingProducts(Long branchId, LocalDate from, LocalDate to, int limit) {

        StringBuilder sb = new StringBuilder(
                "SELECT new com.vegas.sistema_gestion_operativa.sales.application.dto.ProductSalesStatsDto(" +
                        "d.productId, p.name, SUM(d.quantity.value)) " +
                        "FROM SaleDetail d " +
                        "JOIN d.sale s " +
                        "JOIN Product p ON p.id = d.productId " +
                        "WHERE 1 = 1 "
        );

        if (branchId != null) sb.append(" AND s.branchId = :branchId ");
        if (from != null) sb.append(" AND s.saleDate >= :from ");
        if (to != null) sb.append(" AND s.saleDate <= :to ");

        sb.append(" GROUP BY d.productId, p.name ORDER BY SUM(d.quantity.value) ASC");

        return getProductSalesStatsDto(branchId, from, to, limit, sb);
    }

}
