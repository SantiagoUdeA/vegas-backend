package com.vegas.sistema_gestion_operativa.reports.domain.repository;

import com.vegas.sistema_gestion_operativa.products_inventory.application.dto.ProductInventoryMovementDto;
import com.vegas.sistema_gestion_operativa.reports.application.dto.ProductMovementsReportDto;

import java.time.LocalDateTime;
import java.util.List;

public interface IReportsRepository {

    List<ProductInventoryMovementDto> findMovementsForReport(
            Long branchId,
            Long categoryId,
            LocalDateTime fromDate,
            LocalDateTime toDate
    );

    ProductMovementsReportDto createMovementsReport(
            Long branchId,
            Long categoryId,
            LocalDateTime fromDate,
            LocalDateTime toDate,
            String userId
    );
}
