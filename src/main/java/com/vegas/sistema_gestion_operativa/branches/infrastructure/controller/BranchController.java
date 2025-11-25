package com.vegas.sistema_gestion_operativa.branches.infrastructure.controller;

import com.vegas.sistema_gestion_operativa.branches.application.dto.CreateBranchDto;
import com.vegas.sistema_gestion_operativa.branches.application.dto.UpdateBranchDto;
import com.vegas.sistema_gestion_operativa.branches.application.service.BranchService;
import com.vegas.sistema_gestion_operativa.branches.domain.entity.Branch;
import com.vegas.sistema_gestion_operativa.branches.domain.exception.BranchNameAlreadyExistsException;
import com.vegas.sistema_gestion_operativa.common.dto.PageResponse;
import com.vegas.sistema_gestion_operativa.common.dto.PaginationRequest;
import com.vegas.sistema_gestion_operativa.common.exceptions.AccessDeniedException;
import com.vegas.sistema_gestion_operativa.common.utils.PaginationUtils;
import com.vegas.sistema_gestion_operativa.security.AuthUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/branches")
public class BranchController {

    private final BranchService branchService;

    @Autowired
    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @PostMapping
    @PreAuthorize("hasPermission(null, 'BRANCHES_CREATE')")
    public ResponseEntity<Branch> create(@RequestBody @Valid CreateBranchDto dto) throws BranchNameAlreadyExistsException {
        Branch branch = branchService.create(dto, AuthUtils.getUserIdFromToken());
        return ResponseEntity.ok(branch);
    }

    @GetMapping
    @PreAuthorize("hasPermission(null, 'BRANCHES_VIEW')")
    public ResponseEntity<PageResponse<Branch>> findAll(PaginationRequest paginationRequest) {
        Pageable pageable = PaginationUtils.getPageable(paginationRequest);
        PageResponse<Branch> response = PageResponse.from(
                branchService.findOwnerBranches(AuthUtils.getUserIdFromToken(), pageable)
        );
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{branchId}")
    @PreAuthorize("hasPermission(null, 'BRANCHES_EDIT')")
    public ResponseEntity<Branch> update(@PathVariable Long branchId, @RequestBody @Valid UpdateBranchDto dto) throws AccessDeniedException {
        Branch updated = branchService.update(AuthUtils.getUserIdFromToken(), branchId, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/user-branches")
    public ResponseEntity<List<Branch>> findAllBranchesByUserId() {
        List<Branch> branches = branchService.findAllBranchesByUserId(AuthUtils.getUserIdFromToken());
        return ResponseEntity.ok(branches);
    }
}