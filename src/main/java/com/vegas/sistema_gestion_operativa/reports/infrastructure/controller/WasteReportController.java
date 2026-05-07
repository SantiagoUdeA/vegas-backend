package com.vegas.sistema_gestion_operativa.reports.infrastructure.controller;

import com.vegas.sistema_gestion_operativa.common.exceptions.AccessDeniedException;
import com.vegas.sistema_gestion_operativa.common.utils.DateTimeUtils;
import com.vegas.sistema_gestion_operativa.reports.application.dto.GenerateWasteReportDto;
import com.vegas.sistema_gestion_operativa.reports.application.service.WasteReportService;
import com.vegas.sistema_gestion_operativa.reports.domain.exceptions.NoMovementsForReportGenerationException;
import com.vegas.sistema_gestion_operativa.security.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class WasteReportController {

    private final WasteReportService wasteReportService;

    @GetMapping(
            path = "/wastes",
            produces = {MediaType.APPLICATION_PDF_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    @PreAuthorize("hasPermission(null, 'REPORTS_VIEW')")
    public ResponseEntity<byte[]> generateWasteReport(
            @RequestParam Long branchId,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime fromDate,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime toDate
    ) throws AccessDeniedException, NoMovementsForReportGenerationException {

        var dto = new GenerateWasteReportDto(
                branchId,
                DateTimeUtils.toStartOfDay(fromDate),
                DateTimeUtils.toEndOfDay(toDate)
        );

        var report = wasteReportService.generateWasteReport(dto, AuthUtils.getUserIdFromToken());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;")
                .body(report);
    }
}
