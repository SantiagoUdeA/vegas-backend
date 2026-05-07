package com.vegas.sistema_gestion_operativa.production.infrastructure.repository;

import com.vegas.sistema_gestion_operativa.production.domain.entity.ProductionBatchConsumption;
import com.vegas.sistema_gestion_operativa.production.domain.repository.IProductionBatchConsumptionRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductionBatchConsumptionRepositoryJpa extends IProductionBatchConsumptionRepository, JpaRepository<ProductionBatchConsumption, Long> {
}
