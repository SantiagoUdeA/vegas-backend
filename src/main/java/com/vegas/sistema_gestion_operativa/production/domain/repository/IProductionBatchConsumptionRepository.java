package com.vegas.sistema_gestion_operativa.production.domain.repository;

import com.vegas.sistema_gestion_operativa.production.domain.entity.ProductionBatchConsumption;

import java.util.List;

public interface IProductionBatchConsumptionRepository {
    ProductionBatchConsumption save(ProductionBatchConsumption consumption);
    List<ProductionBatchConsumption> findByProductionId(Long productionId);
}
