package com.vegas.sistema_gestion_operativa.catalog.products.infrastructure.controller;

import com.vegas.sistema_gestion_operativa.common.dto.PageResponse;
import com.vegas.sistema_gestion_operativa.common.dto.PaginationRequest;
import com.vegas.sistema_gestion_operativa.common.utils.PaginationUtils;
import com.vegas.sistema_gestion_operativa.catalog.products.application.dto.CreateProductCategoryDto;
import com.vegas.sistema_gestion_operativa.catalog.products.api.ProductCategoryDto;
import com.vegas.sistema_gestion_operativa.catalog.products.application.dto.UpdateProductCategoryDto;
import com.vegas.sistema_gestion_operativa.catalog.products.application.service.ProductCategoryService;
import com.vegas.sistema_gestion_operativa.catalog.products.domain.exceptions.ProductCategoryNameAlreadyExistsException;
import com.vegas.sistema_gestion_operativa.catalog.products.domain.exceptions.ProductCategoryNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for product category management.
 * Provides endpoints to manage product categories.
 */
@RestController
@RequestMapping("/api/v1/product-categories")
public class ProductCategoryController {

    private final ProductCategoryService categoryService;

    @Autowired
    public ProductCategoryController(ProductCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Retrieves the list of all product categories.
     *
     * @param paginationRequest pagination parameters
     * @return paginated list of product categories
     */
    @GetMapping
    @PreAuthorize("hasPermission(null, 'PRODUCTS_VIEW')")
    public ResponseEntity<PageResponse<ProductCategoryDto>> findAll(PaginationRequest paginationRequest) {
        Pageable pageable = PaginationUtils.getPageable(paginationRequest);
        Page<ProductCategoryDto> categoryPage = categoryService.findAll(pageable);
        var response = PageResponse.from(categoryPage);
        return ResponseEntity.ok(response);
    }

    /**
     * Creates a new product category.
     *
     * @param dto data to create the category
     * @return created category
     */
    @PostMapping
    @PreAuthorize("hasPermission(null, 'PRODUCTS_CREATE')")
    public ResponseEntity<ProductCategoryDto> create(@RequestBody @Valid CreateProductCategoryDto dto) throws ProductCategoryNameAlreadyExistsException {
        ProductCategoryDto category = categoryService.create(dto);
        return ResponseEntity.ok(category);
    }

    /**
     * Updates an existing product category.
     *
     * @param categoryId the ID of the category to update
     * @param dto        data to update the category
     * @return updated category
     */
    @PatchMapping("/{categoryId}")
    @PreAuthorize("hasPermission(null, 'PRODUCTS_EDIT')")
    public ResponseEntity<ProductCategoryDto> update(
            @PathVariable Long categoryId,
            @RequestBody @Valid UpdateProductCategoryDto dto
    ) throws ProductCategoryNotFoundException {
        ProductCategoryDto updated = categoryService.update(categoryId, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Deletes a product category.
     *
     * @param categoryId the ID of the category to delete
     * @return deleted category
     */
    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasPermission(null, 'PRODUCTS_DELETE')")
    public ResponseEntity<ProductCategoryDto> delete(@PathVariable Long categoryId) throws ProductCategoryNotFoundException {
        ProductCategoryDto deleted = categoryService.delete(categoryId);
        return ResponseEntity.ok(deleted);
    }
}

