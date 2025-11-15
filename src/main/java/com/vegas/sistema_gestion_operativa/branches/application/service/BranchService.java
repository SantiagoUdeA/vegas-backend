package com.vegas.sistema_gestion_operativa.branches.application.service;

import com.vegas.sistema_gestion_operativa.branches.IBranchApi;
import com.vegas.sistema_gestion_operativa.branches.domain.entity.Branch;
import com.vegas.sistema_gestion_operativa.branches.application.dto.CreateBranchDto;
import com.vegas.sistema_gestion_operativa.branches.application.dto.UpdateBranchDto;
import com.vegas.sistema_gestion_operativa.branches.domain.exception.BranchNameAlreadyExistsException;
import com.vegas.sistema_gestion_operativa.branches.domain.exception.BranchNotFoundException;
import com.vegas.sistema_gestion_operativa.branches.application.factory.BranchFactory;
import com.vegas.sistema_gestion_operativa.branches.application.mapper.IBranchMapper;
import com.vegas.sistema_gestion_operativa.branches.infrastructure.repository.IBranchRepository;
import com.vegas.sistema_gestion_operativa.common.exceptions.AccessDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BranchService implements IBranchApi {

    private final IBranchRepository branchRepository;
    private final IBranchMapper branchMapper;

    @Autowired
    public BranchService(
            IBranchRepository branchRepository,
            IBranchMapper branchMapper) {
        this.branchRepository = branchRepository;
        this.branchMapper = branchMapper;
    }

    @Transactional
    public Branch create(CreateBranchDto dto, String ownerId) throws BranchNameAlreadyExistsException {
        if(this.branchRepository.existsByNameAndUserBranches_Id_UserId(dto.name(), ownerId))
            throw new BranchNameAlreadyExistsException("Ya existe una sede con el nombre: " + dto.name());
        var branch = BranchFactory.createBranch(dto);
        branch.assignFounder(ownerId);
        return branchRepository.save(branch);
    }

    public Page<Branch> findOwnerBranches(String userId, Pageable pageable) {
        return this.branchRepository.findByUserBranches_Id_UserIdAndUserBranches_FounderTrue(userId, pageable);
    }

    public Branch update(String ownerId, Long branchId, UpdateBranchDto dto) throws BranchNotFoundException, AccessDeniedException {
        // TODO Evitar que se carguen datos infinitos anidados en branch (debuggear para ver)
        var branch = this.retrieveBranch(branchId);

        if(!branch.isFoundedByUser(ownerId))
            throw new AccessDeniedException("No puedes editar por que no eres el fundador de esta sucursal");

        var updatedBranch = this.branchMapper.partialUpdate(dto, branch);
        updatedBranch.setId(branchId); // TODO Arreglar esto en todos los servicios
        return this.branchRepository.save(updatedBranch);
    }

    public void assignUserToBranch(String userId, Long branchId, boolean isFounder) {
        var branch = this.retrieveBranch(branchId);
        if(isFounder) branch.assignFounder(userId);
        else branch.assignUser(userId);
        branchRepository.save(branch);
    }

    public Branch retrieveBranch(Long id) {
        return this.branchRepository.findById(id)
                .orElseThrow(() -> new BranchNotFoundException("Branch not found with id: " + id));
    }

    @Override
    public void assertUserHasAccessToBranch(String userId, Long branchId) throws AccessDeniedException {
        var branches = branchRepository.findBranchIdsByUserId(userId);
        if(!branches.contains(branchId))
            throw new AccessDeniedException("No tienes acceso a la sucursal ");
    }

    public List<Branch> findAllBranchesByUserId(String userIdFromToken) {
        return branchRepository.findBranchesByUserId(userIdFromToken);
    }
}
