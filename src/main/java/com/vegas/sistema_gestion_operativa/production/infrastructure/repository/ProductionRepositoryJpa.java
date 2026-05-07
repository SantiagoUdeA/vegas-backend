package com.vegas.sistema_gestion_operativa.production.infrastructure.repository;

import com.vegas.sistema_gestion_operativa.production.domain.entity.Production;
import com.vegas.sistema_gestion_operativa.production.domain.repository.IProductionRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductionRepositoryJpa extends IProductionRepository, JpaRepository<Production, Long> {
}
