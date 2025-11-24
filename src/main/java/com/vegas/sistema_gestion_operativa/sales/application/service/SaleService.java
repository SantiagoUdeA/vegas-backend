package com.vegas.sistema_gestion_operativa.sales.application.service;

import com.vegas.sistema_gestion_operativa.common.exceptions.AccessDeniedException;
import com.vegas.sistema_gestion_operativa.security.AuthUtils;
import com.vegas.sistema_gestion_operativa.sales.application.dto.CreateSaleDto;
import com.vegas.sistema_gestion_operativa.sales.application.factory.SaleFactory;
import com.vegas.sistema_gestion_operativa.sales.domain.entity.Sale;
import com.vegas.sistema_gestion_operativa.sales.domain.repository.ISaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final ISaleRepository saleRepository;
    private final SaleFactory saleFactory;

    @Transactional
    public Sale create(CreateSaleDto dto, String userId) throws AccessDeniedException {
        // Minimal implementation: build Sale from DTO and save.
        // Full validation, stock adjustment and movements will be added later.
        Sale sale = saleFactory.createFromDto(dto);
        // Assign current user as employee
        if (userId == null || userId.isBlank()) {
            userId = AuthUtils.getUserIdFromToken();
        }
        sale.setEmployeeId(userId);
        return saleRepository.save(sale);
    }
}
