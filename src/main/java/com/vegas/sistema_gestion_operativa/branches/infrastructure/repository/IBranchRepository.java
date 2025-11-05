package com.vegas.sistema_gestion_operativa.branches.infrastructure.repository;

import com.vegas.sistema_gestion_operativa.branches.domain.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IBranchRepository extends JpaRepository<Branch, Long> {

    List<Branch> findByUserBranches_Id_UserIdAndUserBranches_FounderTrue(String userId);

    boolean existsByNameAndUserBranches_Id_UserId(String name, String ownerId);

    @Query("SELECT b.id FROM Branch b JOIN b.userBranches ub WHERE ub.id.userId = :userId")
    List<Long> findBranchIdsByUserId(@Param("userId") String userId);

    @Query("SELECT b FROM Branch b JOIN b.userBranches ub WHERE ub.id.userId = :userId")
    List<Branch> findBranchesByUserId(@Param("userId") String userId);
}
