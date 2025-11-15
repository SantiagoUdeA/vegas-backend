
package com.vegas.sistema_gestion_operativa.catalog.products.infrastructure.controller;

import com.vegas.sistema_gestion_operativa.common.dto.PageResponse;
import com.vegas.sistema_gestion_operativa.common.dto.PaginationRequest;
import com.vegas.sistema_gestion_operativa.common.utils.PaginationUtils;
import com.vegas.sistema_gestion_operativa.catalog.products.application.dto.CreateProductDto;
import com.vegas.sistema_gestion_operativa.catalog.products.application.dto.ProductResponseDto;
import com.vegas.sistema_gestion_operativa.catalog.products.application.dto.UpdateProductDto;
import com.vegas.sistema_gestion_operativa.catalog.products.application.service.ProductService;
import com.vegas.sistema_gestion_operativa.catalog.products.domain.exceptions.ProductCategoryNotFoundException;
import com.vegas.sistema_gestion_operativa.catalog.products.domain.exceptions.ProductNameAlreadyExists;
import com.vegas.sistema_gestion_operativa.catalog.products.domain.exceptions.ProductNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
     * Retrieves the list of all products.
    public ResponseEntity<PageResponse<ProductResponseDto>> findAll(PaginationRequest paginationRequest) {
        Pageable pageable = PaginationUtils.getPageable(paginationRequest);
        Page<ProductResponseDto> productPage = productService.findAll(pageable);
        var response = PageResponse.from(productPage);
        return ResponseEntity.ok(response);
     */
    @GetMapping
    @PreAuthorize("hasPermission(null, 'PRODUCTS_VIEW')")
    public ResponseEntity<PageResponse<ProductResponseDto>> findAll(PaginationRequest paginationRequest){
        Pageable pageable = PaginationUtils.getPageable(paginationRequest);
        var response = productService.findAll(pageable);
        return ResponseEntity.ok(PageResponse.from(response));
    }

    /**
     * Creates a new product.
     *
     * @param dto data to create the product
     * @return created product
     */
    @PostMapping
    @PreAuthorize("hasPermission(null, 'PRODUCTS_CREATE')")
    public ResponseEntity<ProductResponseDto> create(@RequestBody @Valid CreateProductDto dto) throws ProductCategoryNotFoundException, ProductNameAlreadyExists {
        ProductResponseDto product = productService.create(dto);
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
    public ResponseEntity<ProductResponseDto> update(
            @PathVariable Long productId,
            @RequestBody @Valid UpdateProductDto dto
    ) throws ProductNotFoundException, ProductCategoryNotFoundException {
        ProductResponseDto updated = productService.update(productId, dto);
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
    public ResponseEntity<ProductResponseDto> delete(@PathVariable Long productId) throws ProductNotFoundException {
        ProductResponseDto deleted = productService.delete(productId);
        return ResponseEntity.ok(deleted);
    }
}

