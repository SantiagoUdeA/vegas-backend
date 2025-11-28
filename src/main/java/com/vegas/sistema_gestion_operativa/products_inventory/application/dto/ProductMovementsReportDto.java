package com.vegas.sistema_gestion_operativa.products_inventory.application.dto;

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
