package com.vegas.sistema_gestion_operativa.raw_material_inventory.infrastructure.controller;

import com.vegas.sistema_gestion_operativa.raw_material_inventory.appliaction.dto.RegisterRawMaterialDto;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.appliaction.service.RawMaterialInventoryService;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.entity.RawMaterialInventory;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
