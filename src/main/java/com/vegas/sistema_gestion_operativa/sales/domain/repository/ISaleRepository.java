package com.vegas.sistema_gestion_operativa.sales.domain.repository;

import com.vegas.sistema_gestion_operativa.sales.domain.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISaleRepository
        extends JpaRepository<Sale, Long>, SaleCustomRepository {
}

