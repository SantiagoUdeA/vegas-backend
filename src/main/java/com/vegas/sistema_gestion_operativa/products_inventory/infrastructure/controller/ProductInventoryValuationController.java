package com.vegas.sistema_gestion_operativa.products_inventory.infrastructure.controller;

import com.vegas.sistema_gestion_operativa.common.exceptions.AccessDeniedException;
import com.vegas.sistema_gestion_operativa.products_inventory.application.dto.ProductValuationResponseDto;
import com.vegas.sistema_gestion_operativa.products_inventory.application.service.ProductInventoryValuationService;
import com.vegas.sistema_gestion_operativa.security.AuthUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/inventory/products/valuation")
public class ProductInventoryValuationController {

    private final ProductInventoryValuationService service;

    public ProductInventoryValuationController(ProductInventoryValuationService service) {
        this.service = service;
    }

    @GetMapping
    public ProductValuationResponseDto getValuation(@RequestParam Long branchId) throws AccessDeniedException {
        return service.calculateValuation(branchId, AuthUtils.getUserIdFromToken());
    }
}
