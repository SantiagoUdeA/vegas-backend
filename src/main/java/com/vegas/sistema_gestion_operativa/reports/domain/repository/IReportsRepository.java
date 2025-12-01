package com.vegas.sistema_gestion_operativa.reports.domain.repository;

import com.vegas.sistema_gestion_operativa.reports.domain.entity.MovementItem;
import com.vegas.sistema_gestion_operativa.reports.domain.entity.MovementReport;
import com.vegas.sistema_gestion_operativa.reports.domain.entity.ProductInventoryReport;
import com.vegas.sistema_gestion_operativa.reports.domain.entity.RawMaterialInventoryReport;
import com.vegas.sistema_gestion_operativa.reports.domain.exceptions.NoMovementsForReportGenerationException;
import jakarta.validation.constraints.NotNull;

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

    ProductInventoryReport createProductInventoryReport(
            @NotNull(message = "La sucursal no puede ser nula") Long branchId,
            Long categoryId,
            String userId
    );

    RawMaterialInventoryReport createRawMaterialInventoryReport(
            @NotNull(message = "La sucursal no puede ser nula") Long branchId,
            Long categoryId,
            String userId
    );
}
