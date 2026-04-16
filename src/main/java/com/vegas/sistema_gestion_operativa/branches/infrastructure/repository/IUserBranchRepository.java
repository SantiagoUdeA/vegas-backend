package com.vegas.sistema_gestion_operativa.branches.infrastructure.repository;

import com.vegas.sistema_gestion_operativa.branches.domain.entity.UserBranch;
import com.vegas.sistema_gestion_operativa.branches.domain.entity.UserBranchId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository for managing UserBranch associations.
 */
public interface IUserBranchRepository extends JpaRepository<UserBranch, UserBranchId> {

    /**
     * Returns all UserBranch associations for a given branch.
     */
    @Query("SELECT ub FROM UserBranch ub WHERE ub.id.branchId = :branchId")
    List<UserBranch> findAllByBranchId(@Param("branchId") Long branchId);

    /**
     * Counts how many UserBranch associations exist for a given branch.
     */
    @Query("SELECT COUNT(ub) FROM UserBranch ub WHERE ub.id.branchId = :branchId")
    long countByBranchId(@Param("branchId") Long branchId);

    /**
     * Checks if a specific user is already assigned to a given branch.
     */
    @Query("SELECT COUNT(ub) > 0 FROM UserBranch ub WHERE ub.id.branchId = :branchId AND ub.id.userId = :userId")
    boolean existsByBranchIdAndUserId(@Param("branchId") Long branchId, @Param("userId") String userId);
}
