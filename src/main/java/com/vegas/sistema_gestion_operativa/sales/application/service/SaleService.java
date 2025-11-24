package com.vegas.sistema_gestion_operativa.sales.application.service;

import com.vegas.sistema_gestion_operativa.common.exceptions.AccessDeniedException;
import com.vegas.sistema_gestion_operativa.security.AuthUtils;
import com.vegas.sistema_gestion_operativa.sales.application.dto.CreateSaleDto;
import com.vegas.sistema_gestion_operativa.sales.application.dto.SaleFilterDto;
import com.vegas.sistema_gestion_operativa.sales.application.dto.SaleResponseDto;
import com.vegas.sistema_gestion_operativa.sales.application.factory.SaleFactory;
import com.vegas.sistema_gestion_operativa.sales.domain.entity.Sale;
import com.vegas.sistema_gestion_operativa.sales.domain.repository.ISaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final ISaleRepository saleRepository;
    private final SaleFactory saleFactory;

    @Transactional
    public Sale create(CreateSaleDto dto, String userId) throws AccessDeniedException {

        Sale sale = saleFactory.createFromDto(dto);

        if (userId == null || userId.isBlank()) {
            userId = AuthUtils.getUserIdFromToken();
        }

        sale.setEmployeeId(userId);

        return saleRepository.save(sale);
    }

    // READ (paginado + filtros)
    @Transactional(readOnly = true)
    public Page<SaleResponseDto> findAll(SaleFilterDto filters, Pageable pageable) {

        Page<Sale> page = saleRepository.findAllFiltered(
                filters.getBranchId(),
                filters.getFrom(),
                filters.getTo(),
                pageable
        );

        return page.map(this::mapToDto);
    }

    private SaleResponseDto mapToDto(Sale sale) {
        return SaleResponseDto.builder()
                .id(sale.getId())
                .saleDate(sale.getSaleDate())
                .total(sale.getTotal())  // <- si total es Money, usa getValue()
                .branchId(sale.getBranchId())
                .employeeId(sale.getEmployeeId())
                .details(
                        sale.getDetails().stream()
                                .map(d -> SaleResponseDto.DetailDto.builder()
                                        .id(d.getId())
                                        .productId(d.getProductId())
                                        .quantity(d.getQuantity()) // si es Quantity
                                        .unitPrice(d.getUnitPrice())           // si es Money
                                        .subtotal(d.getSubtotal())             // si es Money
                                        .build()
                                )
                                .toList()
                )
                .build();
    }

}