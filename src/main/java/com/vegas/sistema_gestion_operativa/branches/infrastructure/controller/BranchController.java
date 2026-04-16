package com.vegas.sistema_gestion_operativa.branches.infrastructure.controller;

import com.vegas.sistema_gestion_operativa.branches.application.dto.AssignBranchOwnerDto;
import com.vegas.sistema_gestion_operativa.branches.application.dto.BranchOwnerResponseDto;
import com.vegas.sistema_gestion_operativa.branches.application.dto.BranchResponseDto;
import com.vegas.sistema_gestion_operativa.branches.application.dto.CreateBranchDto;
import com.vegas.sistema_gestion_operativa.branches.application.dto.UpdateBranchDto;
import com.vegas.sistema_gestion_operativa.branches.application.service.BranchService;
import com.vegas.sistema_gestion_operativa.branches.domain.entity.Branch;
import com.vegas.sistema_gestion_operativa.branches.domain.exception.BranchMustHaveAtLeastOneOwnerException;
import com.vegas.sistema_gestion_operativa.branches.domain.exception.BranchNameAlreadyExistsException;
import com.vegas.sistema_gestion_operativa.branches.domain.exception.BranchOwnerAlreadyAssignedException;
import com.vegas.sistema_gestion_operativa.branches.domain.exception.UserNotOwnerRoleException;
import com.vegas.sistema_gestion_operativa.common.dto.PageResponse;
import com.vegas.sistema_gestion_operativa.common.dto.PaginationRequest;
import com.vegas.sistema_gestion_operativa.common.exceptions.AccessDeniedException;
import com.vegas.sistema_gestion_operativa.common.utils.PaginationUtils;
import com.vegas.sistema_gestion_operativa.franchise.domain.exception.FranchiseAccessDeniedException;
import com.vegas.sistema_gestion_operativa.franchise.domain.exception.FranchiseNotFoundException;
import com.vegas.sistema_gestion_operativa.security.AuthUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/branches")
public class BranchController {

    private static final Logger log = LoggerFactory.getLogger(BranchController.class);

    private final BranchService branchService;

    @Autowired
    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @PostMapping
    @PreAuthorize("hasPermission(null, 'BRANCHES_CREATE')")
    public ResponseEntity<Branch> create(@RequestBody @Valid CreateBranchDto dto)
            throws BranchNameAlreadyExistsException, FranchiseNotFoundException, FranchiseAccessDeniedException {
        Branch branch = branchService.create(dto, AuthUtils.getUserIdFromToken());
        return ResponseEntity.ok(branch);
    }

    @GetMapping
    @PreAuthorize("hasPermission(null, 'BRANCHES_VIEW')")
    public ResponseEntity<PageResponse<BranchResponseDto>> findAll(PaginationRequest paginationRequest) {
        Pageable pageable = PaginationUtils.getPageable(paginationRequest);
        PageResponse<BranchResponseDto> response = PageResponse.from(
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
    public ResponseEntity<List<BranchResponseDto>> findAllBranchesByUserId() {
        if ("ROOT".equals(AuthUtils.getRoleNameFromToken())) {
            return ResponseEntity.ok(List.of());
        }
        try {
            List<BranchResponseDto> branches = branchService.findAllBranchesByUserId(AuthUtils.getUserIdFromToken());
            return ResponseEntity.ok(branches);
        } catch (InvalidDataAccessResourceUsageException e) {
            // Si la tabla branches no existe aún (primera ejecución) o hay un problema
            // de acceso a los datos, retornamos una lista vacía para permitir el login
            log.warn("No se pudieron cargar las branches del usuario: {}", e.getMessage());
            return ResponseEntity.ok(List.of());
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // HU06 — Branch owner management endpoints
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Lists all owners (founder and co-owners) of a branch.
     * Only accessible to users who are already owners of the branch.
     */
    @GetMapping("/{branchId}/owners")
    @PreAuthorize("hasPermission(null, 'BRANCHES_VIEW')")
    public ResponseEntity<List<BranchOwnerResponseDto>> getBranchOwners(
            @PathVariable Long branchId) throws AccessDeniedException {
        String requesterId = AuthUtils.getUserIdFromToken();
        List<BranchOwnerResponseDto> owners = branchService.getOwnersOfBranch(requesterId, branchId);
        return ResponseEntity.ok(owners);
    }

    /**
     * Assigns a new co-owner to a branch.
     * The requester must already be an owner of the branch.
     * The target user must have the OWNER role.
     */
    @PostMapping("/{branchId}/owners")
    @PreAuthorize("hasPermission(null, 'BRANCHES_EDIT')")
    public ResponseEntity<Void> addBranchOwner(
            @PathVariable Long branchId,
            @RequestBody @Valid AssignBranchOwnerDto dto)
            throws AccessDeniedException, UserNotOwnerRoleException, BranchOwnerAlreadyAssignedException {
        String requesterId = AuthUtils.getUserIdFromToken();
        branchService.addOwnerToBranch(requesterId, branchId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Removes a co-owner from a branch.
     * Founders cannot be removed through this endpoint.
     * At least one owner must remain.
     */
    @DeleteMapping("/{branchId}/owners/{targetUserId}")
    @PreAuthorize("hasPermission(null, 'BRANCHES_EDIT')")
    public ResponseEntity<Void> removeBranchOwner(
            @PathVariable Long branchId,
            @PathVariable String targetUserId)
            throws AccessDeniedException, BranchMustHaveAtLeastOneOwnerException {
        String requesterId = AuthUtils.getUserIdFromToken();
        branchService.removeOwnerFromBranch(requesterId, branchId, targetUserId);
        return ResponseEntity.noContent().build();
    }
}