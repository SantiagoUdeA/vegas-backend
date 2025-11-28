package com.vegas.sistema_gestion_operativa.products_inventory.application.factory;

import com.vegas.sistema_gestion_operativa.common.domain.Quantity;
import com.vegas.sistema_gestion_operativa.products_inventory.application.dto.RegisterProductStockDto;
import com.vegas.sistema_gestion_operativa.products_inventory.domain.entity.ProductInventory;
import org.springframework.stereotype.Component;

@Component
public class ProductInventoryFactory {

    public ProductInventory createFromDto(RegisterProductStockDto dto) {
        return ProductInventory.builder()
                .productId(dto.productId())
                .currentStock(new Quantity(0.0))
                .branchId(dto.branchId())
                .build();
    }
}

