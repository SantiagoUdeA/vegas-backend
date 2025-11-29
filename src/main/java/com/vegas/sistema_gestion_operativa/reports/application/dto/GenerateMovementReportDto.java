package com.vegas.sistema_gestion_operativa.reports.application.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public record GenerateMovementReportDto(

        @Param("branchId")
        @NotNull(message = "La sucursal no puede ser nula")
        Long branchId,

        @Param("categoryId")
        Long categoryId,

        @Param("fromDate")
        LocalDateTime fromDate,

        @Param("toDate")
        LocalDateTime toDate

) {
}
