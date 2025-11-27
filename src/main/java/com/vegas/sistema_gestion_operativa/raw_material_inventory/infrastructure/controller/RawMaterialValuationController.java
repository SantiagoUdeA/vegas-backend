package com.vegas.sistema_gestion_operativa.raw_material_inventory.infrastructure.controller;

import com.vegas.sistema_gestion_operativa.common.exceptions.AccessDeniedException;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.application.dto.RawMaterialValuationResponseDto;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.application.service.RawMaterialValuationService;
import com.vegas.sistema_gestion_operativa.security.AuthUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/raw-material-inventory/valuation")
public class RawMaterialValuationController {

    private final RawMaterialValuationService service;

    public RawMaterialValuationController(RawMaterialValuationService service) {
        this.service = service;
    }

    @GetMapping
    public RawMaterialValuationResponseDto getValuation(
            @RequestParam Long branchId) throws AccessDeniedException {
        return service.calculateValuation(branchId, AuthUtils.getUserIdFromToken());
    }
}
