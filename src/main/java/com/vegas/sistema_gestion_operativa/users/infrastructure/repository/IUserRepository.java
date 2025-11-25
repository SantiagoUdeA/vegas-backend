package com.vegas.sistema_gestion_operativa.users.infrastructure.repository;

import com.vegas.sistema_gestion_operativa.users.domain.entity.User;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity.
 * Extends JpaRepository to provide CRUD operations.
 */
public interface IUserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);

    Page<User> findAllByRoleName(String roleName, Pageable pageable);

    /**
     * Finds users that have one of the specified roles and belong to branches
     * where the specified userId also belongs.
     *
     * @param roles  List of role names to filter by
     * @param userId The user ID to match branches with
     * @return List of users matching the criteria
     */
    @Query(
            value = """
                        SELECT DISTINCT u
                        FROM User u
                        JOIN UserBranch ub ON ub.id.userId = u.id
                        WHERE u.roleName IN :roles
                          AND ub.id.branchId IN (
                              SELECT ub2.id.branchId
                              FROM UserBranch ub2
                              WHERE ub2.id.userId = :userId
                          )
                    """,
            countQuery = """
                        SELECT COUNT(DISTINCT u)
                        FROM User u
                        JOIN UserBranch ub ON ub.id.userId = u.id
                        WHERE u.roleName IN :roles
                          AND ub.id.branchId IN (
                              SELECT ub2.id.branchId
                              FROM UserBranch ub2
                              WHERE ub2.id.userId = :userId
                          )
                    """
    )
    Page<User> findUsersByRolesInBranchesWithUser(
            @Param("roles") List<String> roles,
            @Param("userId") String userId,
            Pageable pageable
    ) throws InvalidDataAccessApiUsageException;


}
