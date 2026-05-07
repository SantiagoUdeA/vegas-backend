package com.vegas.sistema_gestion_operativa.reports.application.dto;

import java.time.LocalDateTime;

public record GenerateWasteReportDto(
        Long branchId,
        LocalDateTime fromDate,
        LocalDateTime toDate
) {}
