package com.vegas.sistema_gestion_operativa.reports.domain.repository;

import com.vegas.sistema_gestion_operativa.reports.domain.entity.MovementItem;
import com.vegas.sistema_gestion_operativa.reports.domain.entity.MovementReport;
import com.vegas.sistema_gestion_operativa.reports.domain.exceptions.NoMovementsForReportGenerationException;

import java.time.LocalDateTime;
import java.util.List;

public interface IReportsRepository {

    List<MovementItem> findProductMovements(
            Long branchId,
            Long categoryId,
            LocalDateTime fromDate,
            LocalDateTime toDate
    );

    MovementReport createProductMovementsReport(
            Long branchId,
            Long categoryId,
            LocalDateTime fromDate,
            LocalDateTime toDate,
            String userId
    ) throws NoMovementsForReportGenerationException;

    List<MovementItem> findRawMaterialMovements(
            Long branchId,
            Long categoryId,
            LocalDateTime fromDate,
            LocalDateTime toDate
    );

    MovementReport createRawMaterialMovementsReport(
            Long branchId,
            Long categoryId,
            LocalDateTime fromDate,
            LocalDateTime toDate,
            String userId
    ) throws NoMovementsForReportGenerationException;
}
