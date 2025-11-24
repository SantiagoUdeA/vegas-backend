package com.vegas.sistema_gestion_operativa.raw_material_inventory.application.factory;

import com.vegas.sistema_gestion_operativa.common.domain.Quantity;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.application.dto.RegisterRawMaterialBatchDto;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.entity.MovementReason;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.entity.RawMaterialMovement;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class RawMaterialMovementFactory {

    public RawMaterialMovement createMovementFromDto(RegisterRawMaterialBatchDto dto, Long batchId, String userId) {
        return RawMaterialMovement.builder()
                .movementReason(MovementReason.ENTRADA)
                .rawMaterialBatchId(batchId)
                .rawMaterialId(dto.rawMaterialId())
                .userId(userId)
                .movementDate(LocalDateTime.now())
                .quantity(new Quantity(dto.quantity()))
                .build();
    }

    public RawMaterialMovement createMovementForAdjustment(Long rawMaterialId, Quantity quantity, MovementReason reason, String userId) {
        return RawMaterialMovement.builder()
                .movementReason(reason)
                .rawMaterialId(rawMaterialId)
                .userId(userId)
                .movementDate(LocalDateTime.now())
                .quantity(quantity)
                .build();
    }

    public RawMaterialMovement createMovementForAdjustment(Long rawMaterialId, Quantity quantity, MovementReason reason, String userId, String justification) {
        return RawMaterialMovement.builder()
                .movementReason(reason)
                .rawMaterialId(rawMaterialId)
                .userId(userId)
                .movementDate(LocalDateTime.now())
                .quantity(quantity)
                .justification(justification)
                .build();
    }
}
