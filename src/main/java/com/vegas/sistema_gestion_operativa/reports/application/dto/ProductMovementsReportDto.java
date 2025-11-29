package com.vegas.sistema_gestion_operativa.reports.application.dto;

public record ProductMovementsReportDto(
        String branchName,
        String userName,
        String userRole,
        Long totalEntries,
        Long totalExits,
        Long totalMovements,
        Long totalAdjustments

) {
}
