package com.vegas.sistema_gestion_operativa.raw_material.infrastructure.controller;

import com.vegas.sistema_gestion_operativa.raw_material.application.dto.CreateRawMaterialDto;
import com.vegas.sistema_gestion_operativa.raw_material.application.dto.RawMaterialResponseDto;
import com.vegas.sistema_gestion_operativa.raw_material.application.dto.UpdateRawMaterialDto;
import com.vegas.sistema_gestion_operativa.raw_material.application.service.RawMaterialService;
import com.vegas.sistema_gestion_operativa.raw_material.domain.exceptions.RawMaterialCategoryNotFoundException;
import com.vegas.sistema_gestion_operativa.raw_material.domain.exceptions.RawMaterialNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for raw material management.
 * Provides endpoints to manage raw materials.
 */
@RestController
@RequestMapping("/api/v1/raw-materials")
public class RawMaterialController {

    private final RawMaterialService rawMaterialService;

    @Autowired
    public RawMaterialController(RawMaterialService rawMaterialService) {
        this.rawMaterialService = rawMaterialService;
    }

    /**
     * Retrieves the list of all raw materials.
     *
     * @return list of raw materials
     */
    @GetMapping
    @PreAuthorize("hasPermission(null, 'RAW_MATERIALS_VIEW')")
    public ResponseEntity<List<RawMaterialResponseDto>> findAll() {
        return ResponseEntity.ok(rawMaterialService.findAll());
    }

    /**
     * Creates a new raw material.
     *
     * @param dto data to create the raw material
     * @return created raw material
     */
    @PostMapping
    @PreAuthorize("hasPermission(null, 'RAW_MATERIALS_CREATE')")
    public ResponseEntity<RawMaterialResponseDto> create(@RequestBody @Valid CreateRawMaterialDto dto) throws RawMaterialCategoryNotFoundException {
        RawMaterialResponseDto rawMaterial = rawMaterialService.create(dto);
        return ResponseEntity.ok(rawMaterial);
    }

    /**
     * Updates an existing raw material.
     *
     * @param rawMaterialId the ID of the raw material to update
     * @param dto           data to update the raw material
     * @return updated raw material
     */
    @PatchMapping("/{rawMaterialId}")
    @PreAuthorize("hasPermission(null, 'RAW_MATERIALS_EDIT')")
    public ResponseEntity<RawMaterialResponseDto> update(
            @PathVariable Long rawMaterialId,
            @RequestBody @Valid UpdateRawMaterialDto dto
    ) throws RawMaterialNotFoundException, RawMaterialCategoryNotFoundException {
        RawMaterialResponseDto updated = rawMaterialService.update(rawMaterialId, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Deletes a raw material.
     *
     * @param rawMaterialId the ID of the raw material to delete
     * @return deleted raw material
     */
    @DeleteMapping("/{rawMaterialId}")
    @PreAuthorize("hasPermission(null, 'RAW_MATERIALS_DELETE')")
    public ResponseEntity<RawMaterialResponseDto> delete(@PathVariable Long rawMaterialId) throws RawMaterialNotFoundException {
        RawMaterialResponseDto deleted = rawMaterialService.delete(rawMaterialId);
        return ResponseEntity.ok(deleted);
    }
}
