package com.vegas.sistema_gestion_operativa.reports.domain.repository;

import com.vegas.sistema_gestion_operativa.reports.domain.entity.MovementItem;
import com.vegas.sistema_gestion_operativa.reports.domain.entity.MovementReport;

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
    );
}
