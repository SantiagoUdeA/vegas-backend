package com.vegas.sistema_gestion_operativa.provider.infrastructure.repository;


import com.vegas.sistema_gestion_operativa.provider.domain.entity.Provider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProviderRepository extends JpaRepository<Provider, Long> {
    Page<Provider> findAllByActiveTrue(Pageable pageable);
}
