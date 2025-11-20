package com.vegas.sistema_gestion_operativa.products_inventory.application.mapper;

import com.vegas.sistema_gestion_operativa.products_inventory.application.dto.ProductInventoryResponseDto;
import com.vegas.sistema_gestion_operativa.products_inventory.domain.entity.ProductInventory;
import org.springframework.stereotype.Component;

@Component
public class ProductInventoryMapper {

    public ProductInventoryResponseDto toResponseDto(ProductInventory productInventory) {
        return new ProductInventoryResponseDto(
                productInventory.getId(),
                productInventory.getProductId(),
                productInventory.getCurrentStock().getValue().doubleValue(),
                productInventory.getAverageCost().getValue().doubleValue(),
                productInventory.getUpdatedAt()
        );
    }
}

