
package com.vegas.sistema_gestion_operativa.products.infrastructure.controller;

import com.vegas.sistema_gestion_operativa.products.application.dto.CreateProductDto;
import com.vegas.sistema_gestion_operativa.products.application.dto.ProductResponseDto;
import com.vegas.sistema_gestion_operativa.products.application.dto.UpdateProductDto;
import com.vegas.sistema_gestion_operativa.products.application.service.ProductService;
import com.vegas.sistema_gestion_operativa.products.domain.exceptions.ProductCategoryNotFoundException;
import com.vegas.sistema_gestion_operativa.products.domain.exceptions.ProductNameAlreadyExists;
import com.vegas.sistema_gestion_operativa.products.domain.exceptions.ProductNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     *
     * @return list of products
     */
    @GetMapping
    @PreAuthorize("hasPermission(null, 'PRODUCTS_VIEW')")
    public ResponseEntity<List<ProductResponseDto>> findAll() {
        return ResponseEntity.ok(productService.findAll());
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

