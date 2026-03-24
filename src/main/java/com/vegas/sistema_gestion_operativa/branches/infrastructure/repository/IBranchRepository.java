package com.vegas.sistema_gestion_operativa.branches.infrastructure.repository;

import com.vegas.sistema_gestion_operativa.branches.domain.entity.Branch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IBranchRepository extends JpaRepository<Branch, Long> {

    Page<Branch> findByUserBranches_Id_UserIdAndUserBranches_FounderTrue(String userId, Pageable pageable);

    boolean existsByNameAndUserBranches_Id_UserId(String name, String ownerId);

    @Query("SELECT b.id FROM Branch b JOIN b.userBranches ub WHERE ub.id.userId = :userId")
    List<Long> findBranchIdsByUserId(@Param("userId") String userId);

    @Query("""
        SELECT DISTINCT b FROM Branch b
        LEFT JOIN b.userBranches ub
        WHERE ub.id.userId = :userId
           OR EXISTS (
                SELECT 1 FROM OwnerFranchise of
                WHERE of.ownerId = :userId
                  AND of.franchiseId = b.franchiseId
           )
        ORDER BY b.id
        """)
    List<Branch> findBranchesByUserId(@Param("userId") String userId);
}
