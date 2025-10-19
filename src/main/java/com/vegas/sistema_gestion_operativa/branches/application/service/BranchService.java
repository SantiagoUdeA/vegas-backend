package com.vegas.sistema_gestion_operativa.branches.application.service;

import com.vegas.sistema_gestion_operativa.branches.domain.entity.Branch;
import com.vegas.sistema_gestion_operativa.branches.application.dto.CreateBranchDto;
import com.vegas.sistema_gestion_operativa.branches.application.dto.UpdateBranchDto;
import com.vegas.sistema_gestion_operativa.branches.domain.exception.BranchNameAlreadyExistsException;
import com.vegas.sistema_gestion_operativa.branches.domain.exception.BranchNotFoundException;
import com.vegas.sistema_gestion_operativa.branches.application.factory.BranchFactory;
import com.vegas.sistema_gestion_operativa.branches.application.mapper.IBranchMapper;
import com.vegas.sistema_gestion_operativa.branches.infrastructure.repository.IBranchRepository;
import com.vegas.sistema_gestion_operativa.common.exceptions.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class BranchService {

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

    public List<Branch> findOwnerBranches(String userId) {
        return this.branchRepository.findByUserBranches_Id_UserIdAndUserBranches_FounderTrue(userId);
    }

    public Branch update(String ownerId, Long branchId, UpdateBranchDto dto) throws BranchNotFoundException, AccessDeniedException {
        // TODO Evitar que se carguen datos infinitos anidados en branch (debuggear para ver)
        var branch = this.retrieveBranch(branchId);

        if(!branch.isFoundedByUser(ownerId)) {
            throw new AccessDeniedException("El usuario no está autorizado a modificar esta sucursal");
        }
        var updatedBranch = this.branchMapper.partialUpdate(dto, branch);
        return this.branchRepository.save(updatedBranch);
    }

    public void assignUserToBranch(String userId, Long branchId, boolean isFounder) {
        var branch = this.retrieveBranch(branchId);
        if(isFounder) branch.assignFounder(userId);
        branchRepository.save(branch);
    }

    public Branch retrieveBranch(Long id) {
        return this.branchRepository.findById(id)
                .orElseThrow(() -> new BranchNotFoundException("Branch not found with id: " + id));
    }

    /*public Branch findById(Long id) {
        return this.branchRepository
                .findById(id)
                .orElseThrow(() -> new BranchNotFoundException("Branch not found with id: " + id));
    }



    public Branch delete(Long id){
        var branch = this.findById(id);
        this.branchRepository.deleteById(id);
        return branch;
    }*/
}
