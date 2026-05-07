package com.vegas.sistema_gestion_operativa.production.domain.repository;

import com.vegas.sistema_gestion_operativa.production.domain.entity.Production;

import java.util.List;

public interface IProductionRepository {
    Production save(Production production);
    List<Production> findByBranchIdOrderByProductionDateDesc(Long branchId);
}
