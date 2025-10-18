package com.vegas.sistema_gestion_operativa.branches.infrastructure.repository;

import com.vegas.sistema_gestion_operativa.branches.domain.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IBranchRepository extends JpaRepository<Branch, Long> {

    List<Branch> findByUserBranches_Id_UserIdAndUserBranches_FounderTrue(String userId);

}
