package com.vegas.sistema_gestion_operativa.branches.repository;

import com.vegas.sistema_gestion_operativa.branches.domain.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBranchRepository extends JpaRepository<Branch, String> {
}
