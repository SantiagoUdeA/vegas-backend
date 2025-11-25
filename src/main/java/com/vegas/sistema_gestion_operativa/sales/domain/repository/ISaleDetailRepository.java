package com.vegas.sistema_gestion_operativa.sales.domain.repository;

import com.vegas.sistema_gestion_operativa.sales.domain.entity.SaleDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISaleDetailRepository extends JpaRepository<SaleDetail, Long> {
}
