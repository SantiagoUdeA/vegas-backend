package com.vegas.sistema_gestion_operativa.products_inventory.application.service;

import com.vegas.sistema_gestion_operativa.branches.IBranchApi;
import com.vegas.sistema_gestion_operativa.common.exceptions.AccessDeniedException;
import com.vegas.sistema_gestion_operativa.products_inventory.application.dto.*;
import com.vegas.sistema_gestion_operativa.products_inventory.domain.repository.IProductInventoryRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductInventoryValuationService {

    private final IProductInventoryRepository inventoryRepo;
    private final IBranchApi branchApi;

    public ProductInventoryValuationService(
            IProductInventoryRepository inventoryRepo,
            IBranchApi branchApi
    ) {
        this.inventoryRepo = inventoryRepo;
        this.branchApi = branchApi;
    }

    public ProductValuationResponseDto calculateValuation(
            Long branchId,
            String userId
    ) throws AccessDeniedException {

        branchApi.assertUserHasAccessToBranch(userId, branchId);

        var items = inventoryRepo.findInventoryItemsByBranchId(branchId, null)
                .stream()
                .map(i -> {
                    /*
                    BigDecimal stock = BigDecimal.valueOf(i.getCurrentStock());
                    BigDecimal cost = BigDecimal.valueOf(i.getAverageCost());
                    BigDecimal valuation = stock.multiply(cost);
                    */

                    BigDecimal stock = BigDecimal.valueOf(
                            Optional.ofNullable(i.getCurrentStock()).orElse(0d)
                    );

                    BigDecimal cost = BigDecimal.valueOf(
                            Optional.ofNullable(i.getAverageCost()).orElse(0d)
                    );

                    BigDecimal valuation = stock.multiply(cost);

                    return new ProductValuationItemDto(
                            i.getProductId(),
                            i.getProductName(),
                            i.getCategoryName(),
                            stock,
                            cost,
                            valuation
                    );
                })
                .toList();

        // Agrupar por categoría
        Map<String, BigDecimal> valuationByCategory = items.stream()
                .collect(Collectors.groupingBy(
                        ProductValuationItemDto::categoryName,
                        Collectors.mapping(
                                ProductValuationItemDto::valuation,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));

        // Total global
        BigDecimal totalValuation = items.stream()
                .map(ProductValuationItemDto::valuation)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ProductValuationResponseDto(items, valuationByCategory, totalValuation);
    }
}