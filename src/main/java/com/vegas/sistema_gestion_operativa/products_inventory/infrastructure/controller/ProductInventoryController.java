package com.vegas.sistema_gestion_operativa.products_inventory.infrastructure.controller;

import com.vegas.sistema_gestion_operativa.common.dto.PageResponse;
import com.vegas.sistema_gestion_operativa.common.dto.PaginationRequest;
import com.vegas.sistema_gestion_operativa.common.exceptions.AccessDeniedException;
import com.vegas.sistema_gestion_operativa.common.exceptions.ApiException;
import com.vegas.sistema_gestion_operativa.common.utils.PaginationUtils;
import com.vegas.sistema_gestion_operativa.products.domain.exceptions.ProductNotFoundException;
import com.vegas.sistema_gestion_operativa.products_inventory.application.dto.ProductAdjustmentDto;
import com.vegas.sistema_gestion_operativa.products_inventory.application.dto.ProductInventoryItemDto;
import com.vegas.sistema_gestion_operativa.products_inventory.application.dto.ProductInventoryResponseDto;
import com.vegas.sistema_gestion_operativa.products_inventory.application.dto.RegisterProductStockDto;
import com.vegas.sistema_gestion_operativa.products_inventory.application.service.ProductInventoryService;
import com.vegas.sistema_gestion_operativa.products_inventory.domain.entity.ProductInventory;
import com.vegas.sistema_gestion_operativa.products_inventory.domain.exceptions.InsufficientStockException;
import com.vegas.sistema_gestion_operativa.products_inventory.domain.exceptions.ProductInventoryNotFoundException;
import com.vegas.sistema_gestion_operativa.security.AuthUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for product inventory management.
 * Provides endpoints to manage product inventory operations.
 */
@RestController
@RequestMapping("/api/v1/product-inventory")
public class ProductInventoryController {

    private final ProductInventoryService productInventoryService;

    @Autowired
    public ProductInventoryController(ProductInventoryService productInventoryService) {
        this.productInventoryService = productInventoryService;
    }

    /**
     * Retrieves the product inventory for a specific branch with pagination.
     * Returns detailed information including product name and category.
     *
     * @param paginationRequest pagination parameters
     * @param branchId ID of the branch
     * @return paginated list of product inventory items with product and category information
     */
    @GetMapping
    @PreAuthorize("hasPermission(null, 'INVENTORY_VIEW')")
    public ResponseEntity<PageResponse<ProductInventoryItemDto>> getInventoryByBranchId(
            PaginationRequest paginationRequest,
            @RequestParam @NotNull Long branchId
    ) throws AccessDeniedException {
        Pageable pageable = PaginationUtils.getPageable(paginationRequest);
        Page<ProductInventoryItemDto> page = productInventoryService.getInventoryByBranchId(
                branchId,
                pageable,
                AuthUtils.getUserIdFromToken()
        );
        return ResponseEntity.ok(PageResponse.from(page));
    }

    /**
     * Registers or updates stock for a product.
     * Calculates weighted average cost automatically.
     *
     * @param dto data to register product stock
     * @return updated product inventory
     */
    @PostMapping
    @PreAuthorize("hasPermission(null, 'INVENTORY_EDIT')")
    public ResponseEntity<ProductInventoryResponseDto> registerProductStock(
            @RequestBody @Valid RegisterProductStockDto dto
    ) throws ProductNotFoundException, ApiException {
        var inventory = productInventoryService.registerProductStock(
                dto,
                AuthUtils.getUserIdFromToken()
        );
        return ResponseEntity.ok(inventory);
    }

    /**
     * Realiza un ajuste en el inventario de un producto.
     * Reduce el stock según la cantidad especificada.
     *
     * @param dto datos del ajuste (productId, quantity, reason, justification)
     * @return inventario de producto actualizado
     */
    @PostMapping("/adjustment")
    @PreAuthorize("hasPermission(null, 'INVENTORY_EDIT')")
    public ResponseEntity<ProductInventory> doAdjustment(
            @RequestBody @Valid ProductAdjustmentDto dto
    ) throws InsufficientStockException, AccessDeniedException, ProductInventoryNotFoundException {
        return ResponseEntity.ok(
                this.productInventoryService.doAdjustment(dto, AuthUtils.getUserIdFromToken())
        );
    }
}

