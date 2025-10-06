package com.vegas.sistema_gestion_operativa.branches.controller;

import com.vegas.sistema_gestion_operativa.branches.domain.Branch;
import com.vegas.sistema_gestion_operativa.branches.dto.CreateBranchDto;
import com.vegas.sistema_gestion_operativa.branches.dto.UpdateBranchDto;
import com.vegas.sistema_gestion_operativa.branches.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Branch> create(@RequestBody CreateBranchDto dto) {
        Branch branch = branchService.create(dto);
        return ResponseEntity.ok(branch);
    }

    @GetMapping
    @PreAuthorize("hasPermission(null, 'BRANCHES_VIEW')")
    public ResponseEntity<List<Branch>> findAll() {
        return ResponseEntity.ok(branchService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'BRANCHES_VIEW')")
    public ResponseEntity<Branch> findById(@PathVariable String id) {
        return ResponseEntity.ok(branchService.findById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'BRANCHES_EDIT')")
    public ResponseEntity<Branch> update(@PathVariable String id, @RequestBody UpdateBranchDto dto) {
        // Se asume que UpdateBranchDto tiene un método para establecer el id
        UpdateBranchDto updatedDto = new UpdateBranchDto(id, dto.name(), dto.address(), dto.phoneNumber());
        Branch updated = branchService.update(updatedDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'BRANCHES_DELETE')")
    public ResponseEntity<Branch> delete(@PathVariable String id) {
        Branch deleted = branchService.delete(id);
        return ResponseEntity.ok(deleted);
    }

}