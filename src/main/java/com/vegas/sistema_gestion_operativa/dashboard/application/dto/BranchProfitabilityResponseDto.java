package com.vegas.sistema_gestion_operativa.dashboard.application.dto;

import java.math.BigDecimal;

public record BranchProfitabilityResponseDto(
    Long branchId,
    String branchName,
    BigDecimal totalSales,
    BigDecimal totalCost,
    BigDecimal grossMargin,
    BigDecimal marginPercentage
) {}
