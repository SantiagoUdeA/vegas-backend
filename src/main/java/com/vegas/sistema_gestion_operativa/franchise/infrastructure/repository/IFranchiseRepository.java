package com.vegas.sistema_gestion_operativa.franchise.infrastructure.repository;

import com.vegas.sistema_gestion_operativa.franchise.domain.entity.Franchise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface IFranchiseRepository extends JpaRepository<Franchise, Long> {
    Optional<Franchise> findByName(String name);

    Optional<Franchise> findBySlug(String slug);

    Page<Franchise> findByIdIn(Set<Long> ids, Pageable pageable);
}
