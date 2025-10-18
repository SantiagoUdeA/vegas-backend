package com.vegas.sistema_gestion_operativa.branches.infrastructure.controller;

import com.vegas.sistema_gestion_operativa.branches.domain.entity.Branch;
import com.vegas.sistema_gestion_operativa.branches.application.dto.CreateBranchDto;
import com.vegas.sistema_gestion_operativa.branches.application.dto.UpdateBranchDto;
import com.vegas.sistema_gestion_operativa.branches.application.service.BranchService;
import com.vegas.sistema_gestion_operativa.common.exceptions.ApiException;
import com.vegas.sistema_gestion_operativa.security.AuthUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
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
    public ResponseEntity<Branch> create(@RequestBody @Valid CreateBranchDto dto) throws ApiException {
        Branch branch = branchService.create(dto, AuthUtils.getUserIdFromToken());
        return ResponseEntity.ok(branch);
    }

    @GetMapping
    @PreAuthorize("hasPermission(null, 'BRANCHES_VIEW')")
    public ResponseEntity<List<Branch>> findAll() {
        return ResponseEntity.ok(branchService.findOwnerBranches(AuthUtils.getUserIdFromToken()));
    }

    @PatchMapping("/{branchId}")
    @PreAuthorize("hasPermission(null, 'BRANCHES_EDIT')")
    public ResponseEntity<Branch> update(@PathVariable Long branchId, @RequestBody @Valid UpdateBranchDto dto) throws AccessDeniedException {
        Branch updated = branchService.update(AuthUtils.getUserIdFromToken(), branchId, dto);
        return ResponseEntity.ok(updated);
    }

    /*@GetMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'BRANCHES_VIEW')")
    public ResponseEntity<Branch> findById(@PathVariable String id) {
        return ResponseEntity.ok(branchService.findById(id));
    }



    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'BRANCHES_DELETE')")
    public ResponseEntity<Branch> delete(@PathVariable String id) {
        Branch deleted = branchService.delete(id);
        return ResponseEntity.ok(deleted);
    }*/

}