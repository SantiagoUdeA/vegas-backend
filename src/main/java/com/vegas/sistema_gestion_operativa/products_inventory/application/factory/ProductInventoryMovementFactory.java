package com.vegas.sistema_gestion_operativa.products_inventory.application.factory;

import com.vegas.sistema_gestion_operativa.common.domain.Quantity;
import com.vegas.sistema_gestion_operativa.products_inventory.application.dto.RegisterProductStockDto;
import com.vegas.sistema_gestion_operativa.products_inventory.domain.entity.ProductInventoryMovement;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.entity.MovementReason;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ProductInventoryMovementFactory {

    /**
     * Movimiento por SALIDA (venta)
     */
    public ProductInventoryMovement createSaleMovement(RegisterProductStockDto dto, String userId) {
        return ProductInventoryMovement.builder()
                .productId(dto.productId())
                .movementReason(MovementReason.SALIDA)
                .quantity(new Quantity(dto.quantity()))
                .userId(userId)
                .movementDate(LocalDateTime.now())
                .build();
    }

    /**
     * Movimiento por ENTRADA (registro de stock, ajustes, anulación de ventas, etc.)
     */
    public ProductInventoryMovement createEntryMovement(RegisterProductStockDto dto, String userId) {
        return ProductInventoryMovement.builder()
                .productId(dto.productId())
                .movementReason(MovementReason.ENTRADA)
                .quantity(new Quantity(dto.quantity()))
                .userId(userId)
                .movementDate(LocalDateTime.now())
                .build();
    }

    /**
     * Crea un movimiento de inventario para un ajuste
     *
     * @param productId ID del producto
     * @param quantity cantidad del movimiento
     * @param movementReason razón del movimiento
     * @param userId ID del usuario que realiza el ajuste
     * @param justification justificación del ajuste
     * @return el movimiento creado
     */
    public ProductInventoryMovement createMovementForAdjustment(
            Long productId,
            Quantity quantity,
            MovementReason movementReason,
            String userId,
            String justification
    ) {
        return ProductInventoryMovement.builder()
                .productId(productId)
                .movementReason(movementReason)
                .quantity(quantity)
                .userId(userId)
                .justification(justification)
                .movementDate(LocalDateTime.now())
                .build();
    }

    public ProductInventoryMovement createReturnEntryMovement(RegisterProductStockDto dto, String userId) {
        return ProductInventoryMovement.builder()
                .productId(dto.productId())
                .movementReason(MovementReason.RETORNO)
                .quantity(new Quantity(dto.quantity()))
                .userId(userId)
                .movementDate(LocalDateTime.now())
                .build();
    }
}
