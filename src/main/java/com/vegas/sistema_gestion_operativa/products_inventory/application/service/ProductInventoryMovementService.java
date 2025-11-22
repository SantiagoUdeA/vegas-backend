package com.vegas.sistema_gestion_operativa.products_inventory.application.service;

import com.vegas.sistema_gestion_operativa.branches.IBranchApi;
import com.vegas.sistema_gestion_operativa.common.exceptions.AccessDeniedException;
import com.vegas.sistema_gestion_operativa.products_inventory.application.dto.ProductInventoryMovementDto;
import com.vegas.sistema_gestion_operativa.products_inventory.domain.repository.IProductInventoryMovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductInventoryMovementService {

    private final IProductInventoryMovementRepository productInventoryMovementRepository;
    private final IBranchApi branchApi;

    public Page<ProductInventoryMovementDto> findAll(Pageable pageable, String userId, Long branchId) throws AccessDeniedException {
        branchApi.assertUserHasAccessToBranch(userId, branchId);
        return productInventoryMovementRepository.findAllByBranchId(pageable, branchId);
    }
}
