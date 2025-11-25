package com.vegas.sistema_gestion_operativa.products.infrastructure.controller;

import com.vegas.sistema_gestion_operativa.common.dto.PageResponse;
import com.vegas.sistema_gestion_operativa.common.dto.PaginationRequest;
import com.vegas.sistema_gestion_operativa.common.exceptions.AccessDeniedException;
import com.vegas.sistema_gestion_operativa.common.utils.PaginationUtils;
import com.vegas.sistema_gestion_operativa.products.api.ProductDto;
import com.vegas.sistema_gestion_operativa.products.application.dto.CreateProductDto;
import com.vegas.sistema_gestion_operativa.products.application.dto.UpdateProductDto;
import com.vegas.sistema_gestion_operativa.products.application.service.ProductService;
import com.vegas.sistema_gestion_operativa.products.domain.exceptions.ProductCantBeDeletedException;
import com.vegas.sistema_gestion_operativa.products.domain.exceptions.ProductCategoryNotFoundException;
import com.vegas.sistema_gestion_operativa.products.domain.exceptions.ProductNameAlreadyExists;
import com.vegas.sistema_gestion_operativa.products.domain.exceptions.ProductNotFoundException;
import com.vegas.sistema_gestion_operativa.security.AuthUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for product management.
 * Provides endpoints to manage products.
 */
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Retrieves all products with their recipes and ingredients.
     *
     * @param paginationRequest pagination parameters
     * @param branchId          branch identifier
     * @return paginated list of products with recipes
     */
    @GetMapping
    @PreAuthorize("hasPermission(null, 'PRODUCTS_VIEW')")
    public ResponseEntity<PageResponse<ProductDto>> findAll(
            PaginationRequest paginationRequest,
            @RequestParam Long branchId
    ) throws AccessDeniedException {
        Pageable pageable = PaginationUtils.getPageable(paginationRequest);
        Page<ProductDto> page = productService.findAll(
                pageable,
                AuthUtils.getUserIdFromToken(),
                branchId
        );
        return ResponseEntity.ok(PageResponse.from(page));
    }

    /**
     * Creates a new product.
     *
     * @param dto data to create the product
     * @return created product
     */
    @PostMapping
    @PreAuthorize("hasPermission(null, 'PRODUCTS_CREATE')")
    public ResponseEntity<ProductDto> create(
            @RequestBody @Valid CreateProductDto dto
    ) throws ProductCategoryNotFoundException, ProductNameAlreadyExists, AccessDeniedException {
        ProductDto product = productService.create(dto, AuthUtils.getUserIdFromToken());
        return ResponseEntity.ok(product);
    }

    /**
     * Updates an existing product.
     *
     * @param productId the ID of the product to update
     * @param dto       data to update the product
     * @return updated product
     */
    @PatchMapping("/{productId}")
    @PreAuthorize("hasPermission(null, 'PRODUCTS_EDIT')")
    public ResponseEntity<ProductDto> update(
            @PathVariable Long productId,
            @RequestBody @Valid UpdateProductDto dto
    ) throws ProductNotFoundException, ProductCategoryNotFoundException, AccessDeniedException {
        ProductDto updated = productService.update(productId, dto, AuthUtils.getUserIdFromToken());
        return ResponseEntity.ok(updated);
    }

    /**
     * Deletes a product.
     *
     * @param productId the ID of the product to delete
     * @return deleted product
     */
    @DeleteMapping("/{productId}")
    @PreAuthorize("hasPermission(null, 'PRODUCTS_DELETE')")
    public ResponseEntity<ProductDto> delete(@PathVariable Long productId)
            throws ProductNotFoundException, AccessDeniedException, ProductCantBeDeletedException {

        ProductDto deleted = productService.delete(productId, AuthUtils.getUserIdFromToken());
        return ResponseEntity.ok(deleted);
    }
}

