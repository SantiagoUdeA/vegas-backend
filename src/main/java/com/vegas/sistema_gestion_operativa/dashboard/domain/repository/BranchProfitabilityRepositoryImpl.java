package com.vegas.sistema_gestion_operativa.dashboard.domain.repository;

import com.vegas.sistema_gestion_operativa.dashboard.application.dto.BranchProfitabilityResponseDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BranchProfitabilityRepositoryImpl implements IBranchProfitabilityRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<BranchProfitabilityResponseDto> getBranchProfitability(
            String userId,
            Long franchiseId,
            LocalDateTime from,
            LocalDateTime to) {

        String sql = """
            SELECT
              b.id                              AS branch_id,
              b.name                            AS branch_name,
              COALESCE(s.total_sales, 0)        AS total_sales,
              COALESCE(c.total_cost, 0)         AS total_cost
            FROM branches b
            JOIN user_branch ub
                   ON ub.branch_id = b.id
                  AND ub.user_id = :userId
                  AND ub.founder = true
            LEFT JOIN (
              SELECT branch_id, SUM(total) AS total_sales
              FROM sale
              WHERE sale_date BETWEEN :from AND :to
              GROUP BY branch_id
            ) s ON s.branch_id = b.id
            LEFT JOIN (
              SELECT s.branch_id, SUM(sd.quantity * pi.average_cost) AS total_cost
              FROM sale s
              JOIN sale_detail sd       ON sd.sale_id = s.id
              JOIN product_inventory pi ON pi.product_id = sd.product_id
                                       AND pi.branch_id  = s.branch_id
              WHERE s.sale_date BETWEEN :from AND :to
              GROUP BY s.branch_id
            ) c ON c.branch_id = b.id
            WHERE b.franchise_id = :franchiseId
            ORDER BY b.name
        """;

        Query query = entityManager.createNativeQuery(sql)
                .setParameter("userId", userId)
                .setParameter("franchiseId", franchiseId)
                .setParameter("from", from)
                .setParameter("to", to);

        List<Object[]> results = query.getResultList();
        List<BranchProfitabilityResponseDto> dtos = new ArrayList<>();

        for (Object[] row : results) {
            Long branchId = ((Number) row[0]).longValue();
            String branchName = (String) row[1];
            BigDecimal totalSales = new BigDecimal(row[2].toString());
            BigDecimal totalCost = new BigDecimal(row[3].toString());
            
            BigDecimal grossMargin = totalSales.subtract(totalCost);
            BigDecimal marginPercentage = BigDecimal.ZERO;
            
            if (totalSales.signum() > 0) {
                marginPercentage = grossMargin.divide(totalSales, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
            }

            dtos.add(new BranchProfitabilityResponseDto(
                    branchId,
                    branchName,
                    totalSales,
                    totalCost,
                    grossMargin,
                    marginPercentage
            ));
        }

        return dtos;
    }
}
