package com.vegas.sistema_gestion_operativa.production.domain.repository;

import com.vegas.sistema_gestion_operativa.production.domain.entity.Production;

public interface IProductionRepository {
    Production save(Production production);
}
