package com.vegas.sistema_gestion_operativa.sales.domain.repository;

import com.vegas.sistema_gestion_operativa.sales.application.dto.DailySalesAmountDto;
import com.vegas.sistema_gestion_operativa.sales.application.dto.ProductSalesStatsDto;
import com.vegas.sistema_gestion_operativa.sales.domain.entity.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface SaleCustomRepository {

    Page<Sale> findAllFiltered(Long branchId, LocalDate from, LocalDate to, Pageable pageable);

    List<ProductSalesStatsDto> findAllProductsSalesStats(Long branchId, LocalDate from, LocalDate to);

    List<DailySalesAmountDto> findDailySales(Long branchId, LocalDate from, LocalDate to);
}
