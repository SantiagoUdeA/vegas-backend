package com.vegas.sistema_gestion_operativa.reports.infrastructure.controller;

import com.vegas.sistema_gestion_operativa.common.exceptions.AccessDeniedException;
import com.vegas.sistema_gestion_operativa.common.utils.DateTimeUtils;
import com.vegas.sistema_gestion_operativa.products_inventory.domain.exceptions.NoMovementsForReportGenerationException;
import com.vegas.sistema_gestion_operativa.reports.application.dto.GenerateReportDto;
import com.vegas.sistema_gestion_operativa.reports.application.service.ProductInventoryMovementsReportService;
import com.vegas.sistema_gestion_operativa.security.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/products-inventory/movements-report")
@RequiredArgsConstructor
public class ProductInventoryMovementsReportController {

    private final ProductInventoryMovementsReportService productInventoryMovementsReportService;

    @GetMapping(produces = {MediaType.APPLICATION_PDF_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<byte[]> generateReport(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime fromDate,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime toDate,

            @RequestParam Long branchId,
            @RequestParam(required = false) Long categoryId
    ) throws AccessDeniedException, NoMovementsForReportGenerationException {
        var dto = new GenerateReportDto(
                branchId,
                categoryId,
                DateTimeUtils.toStartOfDay(fromDate),
                DateTimeUtils.toEndOfDay(toDate)
        );

        var report = productInventoryMovementsReportService.generateReport(
                dto,
                AuthUtils.getUserIdFromToken()
        );

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;")
                .body(report);
    }

}
