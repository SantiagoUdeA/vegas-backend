package com.vegas.sistema_gestion_operativa.products_inventory.application.factory;

import com.vegas.sistema_gestion_operativa.common.domain.Quantity;
import com.vegas.sistema_gestion_operativa.products_inventory.application.dto.RegisterProductStockDto;
import com.vegas.sistema_gestion_operativa.products_inventory.domain.entity.ProductInventoryMovement;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.entity.MovementReason;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ProductInventoryMovementFactory {

    public final ProductInventoryMovement createFromDto(RegisterProductStockDto dto, String userId){
        return ProductInventoryMovement.builder()
                .productId(dto.productId())
                .movementReason(MovementReason.PRODUCT_ENTRY)
                .quantity(new Quantity(dto.quantity()))
                .userId(userId)
                .movementDate(LocalDateTime.now())
                .build();
    }
}
