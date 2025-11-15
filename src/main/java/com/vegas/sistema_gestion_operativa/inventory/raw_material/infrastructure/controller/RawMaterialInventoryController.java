package com.vegas.sistema_gestion_operativa.inventory.raw_material.infrastructure.controller;

import com.vegas.sistema_gestion_operativa.common.exceptions.AccessDeniedException;
import com.vegas.sistema_gestion_operativa.inventory.raw_material.appliaction.dto.RawMaterialInventoryItemDto;
import com.vegas.sistema_gestion_operativa.inventory.raw_material.appliaction.dto.RegisterRawMaterialDto;
import com.vegas.sistema_gestion_operativa.inventory.raw_material.appliaction.service.RawMaterialInventoryService;
import com.vegas.sistema_gestion_operativa.inventory.raw_material.domain.entity.RawMaterialInventory;
import com.vegas.sistema_gestion_operativa.security.AuthUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/v1/raw-material-inventory")
public class RawMaterialInventoryController {

    private final RawMaterialInventoryService rawMaterialInventoryService;

    @Autowired
    public RawMaterialInventoryController(RawMaterialInventoryService rawMaterialInventoryService) {
        this.rawMaterialInventoryService = rawMaterialInventoryService;
    }

    @PostMapping
    @PreAuthorize("hasPermission(null, 'INVENTORY_CREATE')")
    public ResponseEntity<RawMaterialInventory> registerRawMaterial(@Valid @RequestBody RegisterRawMaterialDto dto){
        var newEntry = rawMaterialInventoryService.registerRawMaterial(dto);
        return ResponseEntity.ok(newEntry);
    }

    @GetMapping
    @PreAuthorize("hasPermission(null, 'INVENTORY_VIEW')")
    public ResponseEntity<List<RawMaterialInventoryItemDto>> getInventoryByBranchId(@Param("branchId") @NotNull Long branchId) throws AccessDeniedException {
        var inventory = rawMaterialInventoryService.getInventoryByBranchId(branchId, AuthUtils.getUserIdFromToken());
        return ResponseEntity.ok(inventory);
    }
}
