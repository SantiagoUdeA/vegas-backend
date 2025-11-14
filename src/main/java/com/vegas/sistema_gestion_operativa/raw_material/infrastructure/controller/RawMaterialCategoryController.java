package com.vegas.sistema_gestion_operativa.raw_material.infrastructure.controller;

import com.vegas.sistema_gestion_operativa.common.dto.PageResponse;
import com.vegas.sistema_gestion_operativa.common.dto.PaginationRequest;
import com.vegas.sistema_gestion_operativa.common.utils.PaginationUtils;
import com.vegas.sistema_gestion_operativa.raw_material.application.dto.CreateRawMaterialCategoryDto;
import com.vegas.sistema_gestion_operativa.raw_material.application.dto.RawMaterialCategoryResponseDto;
import com.vegas.sistema_gestion_operativa.raw_material.application.dto.UpdateRawMaterialCategoryDto;
import com.vegas.sistema_gestion_operativa.raw_material.application.service.RawMaterialCategoryService;
import com.vegas.sistema_gestion_operativa.raw_material.domain.exceptions.RawMaterialCategoryNameAlreadyExistsException;
import com.vegas.sistema_gestion_operativa.raw_material.domain.exceptions.RawMaterialCategoryNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for raw material category management.
 * Provides endpoints to manage raw material categories.
 */
@RestController
@RequestMapping("/api/v1/raw-material-categories")
public class RawMaterialCategoryController {

    private final RawMaterialCategoryService categoryService;

    @Autowired
    public RawMaterialCategoryController(RawMaterialCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Retrieves the list of all raw material categories.
     *
     * @return list of raw material categories
     */
    @GetMapping
    @PreAuthorize("hasPermission(null, 'RAW_MATERIALS_VIEW')")
    public ResponseEntity<PageResponse<RawMaterialCategoryResponseDto>> findAll(PaginationRequest paginationRequest) {
        Pageable pageable = PaginationUtils.getPageable(paginationRequest);
        Page<RawMaterialCategoryResponseDto> categoryPage = categoryService.findAll(pageable);
        var response = PageResponse.from(categoryPage);
        return ResponseEntity.ok(response);
    }

    /**
     * Creates a new raw material category.
     *
     * @param dto data to create the category
     * @return created category
     */
    @PostMapping
    @PreAuthorize("hasPermission(null, 'RAW_MATERIALS_CREATE')")
    public ResponseEntity<RawMaterialCategoryResponseDto> create(@RequestBody @Valid CreateRawMaterialCategoryDto dto) throws RawMaterialCategoryNameAlreadyExistsException {
        RawMaterialCategoryResponseDto category = categoryService.create(dto);
        return ResponseEntity.ok(category);
    }

    /**
     * Updates an existing raw material category.
     *
     * @param categoryId the ID of the category to update
     * @param dto        data to update the category
     * @return updated category
     */
    @PatchMapping("/{categoryId}")
    @PreAuthorize("hasPermission(null, 'RAW_MATERIALS_EDIT')")
    public ResponseEntity<RawMaterialCategoryResponseDto> update(
            @PathVariable Long categoryId,
            @RequestBody @Valid UpdateRawMaterialCategoryDto dto
    ) throws RawMaterialCategoryNotFoundException {
        RawMaterialCategoryResponseDto updated = categoryService.update(categoryId, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Deletes a raw material category.
     *
     * @param categoryId the ID of the category to delete
     * @return deleted category
     */
    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasPermission(null, 'RAW_MATERIALS_DELETE')")
    public ResponseEntity<RawMaterialCategoryResponseDto> delete(@PathVariable Long categoryId) throws RawMaterialCategoryNotFoundException {
        RawMaterialCategoryResponseDto deleted = categoryService.delete(categoryId);
        return ResponseEntity.ok(deleted);
    }
}

