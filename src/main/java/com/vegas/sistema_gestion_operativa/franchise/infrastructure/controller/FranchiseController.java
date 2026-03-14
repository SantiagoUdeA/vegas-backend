package com.vegas.sistema_gestion_operativa.franchise.infrastructure.controller;

import com.vegas.sistema_gestion_operativa.common.dto.PageResponse;
import com.vegas.sistema_gestion_operativa.common.dto.PaginationRequest;
import com.vegas.sistema_gestion_operativa.common.utils.PaginationUtils;
import com.vegas.sistema_gestion_operativa.franchise.application.dto.CreateFranchiseDto;
import com.vegas.sistema_gestion_operativa.franchise.application.dto.FranchiseResponseDto;
import com.vegas.sistema_gestion_operativa.franchise.application.dto.UpdateFranchiseDto;
import com.vegas.sistema_gestion_operativa.franchise.application.service.FranchiseService;
import com.vegas.sistema_gestion_operativa.franchise.domain.exception.FranchiseNameAlreadyExistsException;
import com.vegas.sistema_gestion_operativa.franchise.domain.exception.FranchiseNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/franchises")
public class FranchiseController {

    private final FranchiseService franchiseService;

    @Autowired
    public FranchiseController(FranchiseService franchiseService) {
        this.franchiseService = franchiseService;
    }

    @GetMapping
    @PreAuthorize("hasPermission(null, 'FRANCHISES_VIEW')")
    public ResponseEntity<PageResponse<FranchiseResponseDto>> findAll(PaginationRequest paginationRequest) {
        Pageable pageable = PaginationUtils.getPageable(paginationRequest);
        Page<FranchiseResponseDto> page = franchiseService.findAll(pageable);
        return ResponseEntity.ok(PageResponse.from(page));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'FRANCHISES_VIEW')")
    public ResponseEntity<FranchiseResponseDto> findById(@PathVariable Long id) throws FranchiseNotFoundException {
        return ResponseEntity.ok(franchiseService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasPermission(null, 'FRANCHISES_CREATE')")
    public ResponseEntity<FranchiseResponseDto> create(@RequestBody @Valid CreateFranchiseDto dto) throws FranchiseNameAlreadyExistsException {
        return ResponseEntity.ok(franchiseService.create(dto));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'FRANCHISES_EDIT')")
    public ResponseEntity<FranchiseResponseDto> update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateFranchiseDto dto
    ) throws FranchiseNotFoundException {
        return ResponseEntity.ok(franchiseService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'FRANCHISES_DELETE')")
    public ResponseEntity<FranchiseResponseDto> delete(@PathVariable Long id) throws FranchiseNotFoundException {
        return ResponseEntity.ok(franchiseService.delete(id));
    }
}
