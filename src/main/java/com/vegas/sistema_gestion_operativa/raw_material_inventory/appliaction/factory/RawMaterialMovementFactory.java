package com.vegas.sistema_gestion_operativa.raw_material_inventory.appliaction.factory;

import com.vegas.sistema_gestion_operativa.common.domain.Quantity;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.appliaction.dto.RegisterRawMaterialBatchDto;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.entity.MovementReason;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.entity.RawMaterialMovement;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class RawMaterialMovementFactory {

    public RawMaterialMovement createMovementFromDto(RegisterRawMaterialBatchDto dto, Long batchId, String userId) {
        return RawMaterialMovement.builder()
                .movementReason(MovementReason.ENTRY)
                .rawMaterialBatchId(batchId)
                .userId(userId)
                .movementDate(LocalDateTime.now())
                .quantity(new Quantity(dto.quantity()))
                .build();

    }
}
