package com.vegas.sistema_gestion_operativa.sales.infrastructure.controller;

import com.vegas.sistema_gestion_operativa.common.exceptions.AccessDeniedException;
import com.vegas.sistema_gestion_operativa.sales.application.dto.CreateSaleDto;
import com.vegas.sistema_gestion_operativa.sales.application.service.SaleService;
import com.vegas.sistema_gestion_operativa.sales.domain.entity.Sale;
import com.vegas.sistema_gestion_operativa.security.AuthUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Sale> create(@RequestBody @Valid CreateSaleDto dto) throws AccessDeniedException {
        Sale sale = saleService.create(dto, AuthUtils.getUserIdFromToken());
        return ResponseEntity.ok(sale);
    }
}

