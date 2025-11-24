package com.vegas.sistema_gestion_operativa.sales.domain.repository;

import com.vegas.sistema_gestion_operativa.sales.domain.entity.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface SaleCustomRepository {

    Page<Sale> findAllFiltered(Long branchId, LocalDate from, LocalDate to, Pageable pageable);

}
