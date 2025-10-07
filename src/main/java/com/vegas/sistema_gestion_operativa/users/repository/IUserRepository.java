package com.vegas.sistema_gestion_operativa.users.repository;

import com.vegas.sistema_gestion_operativa.users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for User entity.
 * Extends JpaRepository to provide CRUD operations.
 */
public interface IUserRepository extends JpaRepository<User, String> {
}
