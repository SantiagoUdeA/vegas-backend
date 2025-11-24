package com.vegas.sistema_gestion_operativa.users.domain.repository;

import com.vegas.sistema_gestion_operativa.users.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
