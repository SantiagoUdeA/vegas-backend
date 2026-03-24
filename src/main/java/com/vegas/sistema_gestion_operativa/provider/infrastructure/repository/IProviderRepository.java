package com.vegas.sistema_gestion_operativa.provider.infrastructure.repository;


import com.vegas.sistema_gestion_operativa.provider.domain.entity.Provider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IProviderRepository extends JpaRepository<Provider, Long> {
    Page<Provider> findAllByActiveTrueAndBranchId(Long branchId, Pageable pageable);
    Optional<Provider> findByNitAndBranchId(String nit, Long branchId);
}
