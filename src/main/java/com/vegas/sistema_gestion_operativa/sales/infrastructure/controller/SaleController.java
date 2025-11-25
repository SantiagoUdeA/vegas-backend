package com.vegas.sistema_gestion_operativa.sales.infrastructure.controller;

import com.vegas.sistema_gestion_operativa.common.exceptions.AccessDeniedException;
import com.vegas.sistema_gestion_operativa.common.exceptions.ApiException;
import com.vegas.sistema_gestion_operativa.sales.application.dto.CreateSaleDto;
import com.vegas.sistema_gestion_operativa.sales.application.dto.SaleFilterDto;
import com.vegas.sistema_gestion_operativa.sales.application.dto.SaleResponseDto;
import com.vegas.sistema_gestion_operativa.sales.application.service.SaleService;
import com.vegas.sistema_gestion_operativa.sales.domain.entity.Sale;
import com.vegas.sistema_gestion_operativa.security.AuthUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/sales")
public class SaleController {

    private final SaleService saleService;

    @Autowired
    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping
    @PreAuthorize("hasPermission(null, 'SALES_CREATE')")
    public ResponseEntity<Sale> create(@RequestBody @Valid CreateSaleDto dto)
            throws AccessDeniedException, ApiException {
        Sale sale = saleService.create(dto, AuthUtils.getUserIdFromToken());
        return ResponseEntity.ok(sale);
    }


    @GetMapping
    @PreAuthorize("hasPermission(null, 'SALES_READ')")
    public ResponseEntity<Page<SaleResponseDto>> findAll(
            @RequestParam(required = false) Long branchId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        SaleFilterDto filters = new SaleFilterDto();
        filters.setBranchId(branchId);
        filters.setFrom(from);
        filters.setTo(to);

        Page<SaleResponseDto> result = saleService.findAll(filters, PageRequest.of(page, size));
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'SALES_DELETE')")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) throws AccessDeniedException, ApiException {

        saleService.deleteSale(id, AuthUtils.getUserIdFromToken());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats/product-sales")
    @PreAuthorize("hasPermission(null, 'SALES_READ')")
    public ResponseEntity<?> getProductSalesStats(
            @RequestParam(required = false) Long branchId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return ResponseEntity.ok(
                saleService.getProductSalesStats(branchId, from, to)
        );
    }
}