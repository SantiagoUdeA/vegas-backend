package com.vegas.sistema_gestion_operativa.products_inventory.infrastructure.controller;

import com.vegas.sistema_gestion_operativa.products_inventory.application.dto.ProductValuationResponseDto;
import com.vegas.sistema_gestion_operativa.products_inventory.application.service.ProductInventoryValuationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inventory/products/valuation")
public class ProductInventoryValuationController {

    private final ProductInventoryValuationService service;

    public ProductInventoryValuationController(ProductInventoryValuationService service) {
        this.service = service;
    }

    @GetMapping
    public ProductValuationResponseDto getValuation(
            @RequestParam Long branchId,
            @RequestHeader("X-User-Id") String userId
    ) throws Exception {
        return service.calculateValuation(branchId, userId);
    }
}
