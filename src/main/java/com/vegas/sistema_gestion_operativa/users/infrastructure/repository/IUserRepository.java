package com.vegas.sistema_gestion_operativa.users.infrastructure.repository;

import com.vegas.sistema_gestion_operativa.users.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for User entity.
 * Extends JpaRepository to provide CRUD operations.
 */
public interface IUserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
}
