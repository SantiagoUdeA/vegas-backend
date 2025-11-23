package com.vegas.sistema_gestion_operativa.raw_material_inventory.infrastructure.controller;

import com.vegas.sistema_gestion_operativa.common.dto.PageResponse;
import com.vegas.sistema_gestion_operativa.common.dto.PaginationRequest;
import com.vegas.sistema_gestion_operativa.common.exceptions.AccessDeniedException;
import com.vegas.sistema_gestion_operativa.common.utils.PaginationUtils;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.application.dto.RawMaterialBatchDto;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.application.dto.RegisterRawMaterialBatchDto;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.application.service.RawMaterialInventoryService;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.entity.RawMaterialBatch;
import com.vegas.sistema_gestion_operativa.security.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/raw-material-batches")
public class RawMaterialBatchController {

    private final RawMaterialInventoryService rawMaterialInventoryService;

    @Autowired
    public RawMaterialBatchController(RawMaterialInventoryService rawMaterialInventoryService) {
        this.rawMaterialInventoryService = rawMaterialInventoryService;
    }

    @PostMapping
    public ResponseEntity<RawMaterialBatch> registerRawMaterialBatch(@RequestBody RegisterRawMaterialBatchDto dto) {
        return ResponseEntity.ok(
            this.rawMaterialInventoryService.registerRawMaterialBatch(
                    dto,
                    AuthUtils.getUserIdFromToken()
            )
        );
    }

    @GetMapping
    public ResponseEntity<PageResponse<RawMaterialBatchDto>> getRawMaterialBatches(
            PaginationRequest paginationRequest,
            @RequestParam Long branchId
    ) throws AccessDeniedException {
        Pageable pageable = PaginationUtils.getPageable(paginationRequest);
        Page<RawMaterialBatchDto> rawMaterialBatchPage = rawMaterialInventoryService.findRawMaterialBatchesByBranchId(
                branchId,
                pageable,
                AuthUtils.getUserIdFromToken()
        );
        return ResponseEntity.ok(PageResponse.from(rawMaterialBatchPage));
    }
}
