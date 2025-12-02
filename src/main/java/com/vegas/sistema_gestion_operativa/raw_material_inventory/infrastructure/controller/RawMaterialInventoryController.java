package com.vegas.sistema_gestion_operativa.raw_material_inventory.infrastructure.controller;

import com.vegas.sistema_gestion_operativa.common.dto.PageResponse;
import com.vegas.sistema_gestion_operativa.common.dto.PaginationRequest;
import com.vegas.sistema_gestion_operativa.common.exceptions.AccessDeniedException;
import com.vegas.sistema_gestion_operativa.common.utils.PaginationUtils;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.application.dto.RawMaterialAdjustmentDto;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.application.dto.RawMaterialInventoryItemDto;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.application.service.RawMaterialInventoryService;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.entity.RawMaterialInventory;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.exceptions.InventoryItemNotFoundException;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.exceptions.NotEnoughRawMaterialStockException;
import com.vegas.sistema_gestion_operativa.security.AuthUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/raw-material-inventory")
public class RawMaterialInventoryController {

    private final RawMaterialInventoryService rawMaterialInventoryService;

    @Autowired
    public RawMaterialInventoryController(RawMaterialInventoryService rawMaterialInventoryService) {
        this.rawMaterialInventoryService = rawMaterialInventoryService;
    }

    /**
     * Obtiene el inventario de materias primas de una sede específica con paginación.
     * Retorna información detallada incluyendo nombre de la materia prima y categoría.
     *
     * @param paginationRequest parámetros de paginación
     * @param branchId          ID de la sede
     * @return lista paginada de elementos del inventario con información de materia prima y categoría
     */
    @GetMapping
    @PreAuthorize("hasPermission(null, 'INVENTORY_VIEW')")
    public ResponseEntity<PageResponse<RawMaterialInventoryItemDto>> getInventoryByBranchId(
            PaginationRequest paginationRequest,
            @RequestParam @NotNull Long branchId
    ) throws AccessDeniedException {
        Pageable pageable = PaginationUtils.getPageable(paginationRequest);
        Page<RawMaterialInventoryItemDto> page = rawMaterialInventoryService.getInventoryByBranchId(
                branchId,
                pageable,
                AuthUtils.getUserIdFromToken()
        );
        return ResponseEntity.ok(PageResponse.from(page));
    }

    @PostMapping("/adjustment")
    @PreAuthorize("hasPermission(null, 'INVENTORY_EDIT')")
    public ResponseEntity<RawMaterialInventory> doAdjustment(@RequestBody @Valid RawMaterialAdjustmentDto dto) throws NotEnoughRawMaterialStockException, AccessDeniedException, InventoryItemNotFoundException {
        return ResponseEntity.ok(this.rawMaterialInventoryService.doAdjustment(dto, AuthUtils.getUserIdFromToken()));
    }

    @GetMapping("/low-stock")
    @PreAuthorize("hasPermission(null, 'INVENTORY_VIEW')")
    public ResponseEntity<?> getLowStockAlerts(
            @RequestParam @NotNull Long branchId,
            @RequestParam(defaultValue = "10") Integer minStock
    ) {
        return ResponseEntity.ok(
                rawMaterialInventoryService.getLowStockAlerts(branchId, minStock)
        );
    }
}
