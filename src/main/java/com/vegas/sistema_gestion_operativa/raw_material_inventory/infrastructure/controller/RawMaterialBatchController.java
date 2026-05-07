package com.vegas.sistema_gestion_operativa.raw_material_inventory.infrastructure.controller;

import com.vegas.sistema_gestion_operativa.common.dto.PageResponse;
import com.vegas.sistema_gestion_operativa.common.dto.PaginationRequest;
import com.vegas.sistema_gestion_operativa.common.exceptions.AccessDeniedException;
import com.vegas.sistema_gestion_operativa.common.utils.PaginationUtils;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.application.dto.*;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.application.service.BatchHistoryService;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.application.service.RawMaterialInventoryService;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.entity.RawMaterialBatch;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.exceptions.BatchNotFoundException;
import com.vegas.sistema_gestion_operativa.security.AuthUtils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/raw-material-batches")
public class RawMaterialBatchController {

    private final RawMaterialInventoryService rawMaterialInventoryService;
    private final BatchHistoryService batchHistoryService;

    @Autowired
    public RawMaterialBatchController(
            RawMaterialInventoryService rawMaterialInventoryService,
            BatchHistoryService batchHistoryService
    ) {
        this.rawMaterialInventoryService = rawMaterialInventoryService;
        this.batchHistoryService = batchHistoryService;
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

    // ── HU13 ────────────────────────────────────────────────────────────────

    /**
     * GET /api/v1/raw-material-batches/{batchId}/history
     *
     * <p>Returns the full movement history of a single batch, including running
     * balance after each movement and references to production records.</p>
     *
     * <p>Requires: {@code INVENTORY_VIEW} permission + access to the batch's branch.</p>
     */
    @GetMapping("/{batchId}/history")
    @PreAuthorize("hasPermission(null, 'INVENTORY_VIEW')")
    public ResponseEntity<BatchHistoryResponseDto> getBatchHistory(
            @PathVariable Long batchId,
            PaginationRequest paginationRequest
    ) throws AccessDeniedException, BatchNotFoundException {
        Pageable pageable = PaginationUtils.getPageable(paginationRequest);
        BatchHistoryResponseDto response = batchHistoryService.getBatchHistory(
                batchId,
                pageable,
                AuthUtils.getUserIdFromToken()
        );
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/raw-material-batches/search?branchId=&q=
     *
     * <p>Case-insensitive partial search on batch number, scoped to a branch.</p>
     *
     * <p>Requires: {@code INVENTORY_VIEW} permission + access to the branch.</p>
     */
    @GetMapping("/search")
    @PreAuthorize("hasPermission(null, 'INVENTORY_VIEW')")
    public ResponseEntity<PageResponse<BatchSearchResultDto>> searchBatches(
            @RequestParam @NotNull Long branchId,
            @RequestParam @NotBlank String q,
            PaginationRequest paginationRequest
    ) throws AccessDeniedException {
        Pageable pageable = PaginationUtils.getPageable(paginationRequest);
        PageResponse<BatchSearchResultDto> response = batchHistoryService.searchBatches(
                branchId,
                q,
                pageable,
                AuthUtils.getUserIdFromToken()
        );
        return ResponseEntity.ok(response);
    }
}
