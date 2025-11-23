package com.vegas.sistema_gestion_operativa.raw_material_inventory.infrastructure.controller;

import com.vegas.sistema_gestion_operativa.raw_material_inventory.application.dto.RawMaterialValuationResponseDto;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.application.service.RawMaterialValuationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/raw-material-inventory/valuation")
public class RawMaterialValuationController {

    private final RawMaterialValuationService service;

    public RawMaterialValuationController(RawMaterialValuationService service) {
        this.service = service;
    }

    @GetMapping
    public RawMaterialValuationResponseDto getValuation(
            @RequestParam Long branchId,
            @RequestHeader("X-User-Id") String userId
    ) throws Exception {
        return service.calculateValuation(branchId, userId);
    }
}
