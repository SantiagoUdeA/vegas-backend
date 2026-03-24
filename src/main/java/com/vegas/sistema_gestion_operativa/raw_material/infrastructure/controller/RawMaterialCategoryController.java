package com.vegas.sistema_gestion_operativa.raw_material.infrastructure.controller;

import com.vegas.sistema_gestion_operativa.common.dto.PageResponse;
import com.vegas.sistema_gestion_operativa.common.dto.PaginationRequest;
import com.vegas.sistema_gestion_operativa.common.exceptions.AccessDeniedException;
import com.vegas.sistema_gestion_operativa.common.utils.PaginationUtils;
import com.vegas.sistema_gestion_operativa.raw_material.application.dto.CreateRawMaterialCategoryDto;
import com.vegas.sistema_gestion_operativa.raw_material.application.dto.RawMaterialCategoryResponseDto;
import com.vegas.sistema_gestion_operativa.raw_material.application.dto.UpdateRawMaterialCategoryDto;
import com.vegas.sistema_gestion_operativa.raw_material.application.service.RawMaterialCategoryService;
import com.vegas.sistema_gestion_operativa.raw_material.domain.exceptions.RawMaterialCategoryNameAlreadyExistsException;
import com.vegas.sistema_gestion_operativa.raw_material.domain.exceptions.RawMaterialCategoryNotFoundException;
import com.vegas.sistema_gestion_operativa.security.AuthUtils;
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
     * Retrieves the list of all raw material categories for a branch.
     *
     * @param paginationRequest pagination parameters
     * @param branchId          branch identifier
     * @return paginated list of raw material categories
     */
    @GetMapping
    @PreAuthorize("hasPermission(null, 'RAW_MATERIALS_VIEW')")
    public ResponseEntity<PageResponse<RawMaterialCategoryResponseDto>> findAll(
            PaginationRequest paginationRequest,
            @RequestParam Long branchId
    ) throws AccessDeniedException {
        Pageable pageable = PaginationUtils.getPageable(paginationRequest);
        Page<RawMaterialCategoryResponseDto> categoryPage = categoryService.findAll(
                pageable,
                AuthUtils.getUserIdFromToken(),
                branchId
        );
        return ResponseEntity.ok(PageResponse.from(categoryPage));
    }

    /**
     * Creates a new raw material category.
     *
     * @param dto data to create the category
     * @return created category
     */
    @PostMapping
    @PreAuthorize("hasPermission(null, 'RAW_MATERIALS_CREATE')")
    public ResponseEntity<RawMaterialCategoryResponseDto> create(@RequestBody @Valid CreateRawMaterialCategoryDto dto)
            throws RawMaterialCategoryNameAlreadyExistsException, AccessDeniedException {
        RawMaterialCategoryResponseDto category = categoryService.create(dto, AuthUtils.getUserIdFromToken());
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
    ) throws RawMaterialCategoryNotFoundException, AccessDeniedException {
        RawMaterialCategoryResponseDto updated = categoryService.update(categoryId, dto, AuthUtils.getUserIdFromToken());
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
    public ResponseEntity<RawMaterialCategoryResponseDto> delete(@PathVariable Long categoryId)
            throws RawMaterialCategoryNotFoundException, AccessDeniedException {
        RawMaterialCategoryResponseDto deleted = categoryService.delete(categoryId, AuthUtils.getUserIdFromToken());
        return ResponseEntity.ok(deleted);
    }
}
