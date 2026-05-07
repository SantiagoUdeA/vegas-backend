package com.vegas.sistema_gestion_operativa.production.infrastructure.controller;

import com.vegas.sistema_gestion_operativa.common.exceptions.ApiException;
import com.vegas.sistema_gestion_operativa.production.application.dto.ProductionResponseDto;
import com.vegas.sistema_gestion_operativa.production.application.dto.RawMaterialShortageDto;
import com.vegas.sistema_gestion_operativa.production.application.dto.RegisterProductionDto;
import com.vegas.sistema_gestion_operativa.production.application.service.ProductionService;
import com.vegas.sistema_gestion_operativa.production.domain.exceptions.InsufficientRawMaterialException;
import com.vegas.sistema_gestion_operativa.security.AuthUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/production")
public class ProductionController {

    private final ProductionService productionService;

    @Autowired
    public ProductionController(ProductionService productionService) {
        this.productionService = productionService;
    }

    @PostMapping
    @PreAuthorize("hasPermission(null, 'INVENTORY_EDIT')")
    public ResponseEntity<ProductionResponseDto> registerProduction(
            @RequestBody @Valid RegisterProductionDto dto
    ) throws ApiException {
        String userId = AuthUtils.getUserIdFromToken();
        ProductionResponseDto response = productionService.registerProduction(dto, userId);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(InsufficientRawMaterialException.class)
    public ResponseEntity<Map<String, Object>> handleInsufficientRawMaterial(InsufficientRawMaterialException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                "error", "INSUFFICIENT_RAW_MATERIAL",
                "message", ex.getMessage(),
                "shortages", ex.getShortages()
        ));
    }
}
