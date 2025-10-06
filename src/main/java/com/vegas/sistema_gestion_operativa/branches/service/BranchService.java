package com.vegas.sistema_gestion_operativa.branches.service;

import com.vegas.sistema_gestion_operativa.branches.domain.Branch;
import com.vegas.sistema_gestion_operativa.branches.dto.CreateBranchDto;
import com.vegas.sistema_gestion_operativa.branches.dto.UpdateBranchDto;
import com.vegas.sistema_gestion_operativa.branches.exception.BranchNotFoundException;
import com.vegas.sistema_gestion_operativa.branches.factory.BranchFactory;
import com.vegas.sistema_gestion_operativa.branches.repository.IBranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BranchService {


    private final IBranchRepository branchRepository;

    @Autowired
    public BranchService(IBranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    public Branch create(CreateBranchDto dto) {
        return this.branchRepository.save(BranchFactory.createBranch(dto));
    }

    public List<Branch> findAll() {
        return this.branchRepository.findAll();
    }

    public Branch findById(String id) {
        return this.branchRepository
                .findById(id)
                .orElseThrow(() -> new BranchNotFoundException("Branch not found with id: " + id));
    }

    public Branch update(UpdateBranchDto dto){
        var repository = this.findById(dto.id());
        repository.setName(dto.name());
        repository.setAddress(dto.address());
        repository.setPhoneNumber(dto.phoneNumber());
        return this.branchRepository.save(repository);
    }

    public Branch delete(String id){
        var branch = this.findById(id);
        this.branchRepository.deleteById(id);
        return branch;
    }
}
