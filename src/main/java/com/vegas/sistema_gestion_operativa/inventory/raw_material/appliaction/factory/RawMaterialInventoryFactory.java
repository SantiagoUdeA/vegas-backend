package com.vegas.sistema_gestion_operativa.inventory.raw_material.appliaction.factory;

import com.vegas.sistema_gestion_operativa.common.domain.Money;
import com.vegas.sistema_gestion_operativa.common.domain.Quantity;
import com.vegas.sistema_gestion_operativa.inventory.raw_material.appliaction.dto.RegisterRawMaterialBatchDto;
import com.vegas.sistema_gestion_operativa.inventory.raw_material.appliaction.dto.RegisterRawMaterialDto;
import com.vegas.sistema_gestion_operativa.inventory.raw_material.domain.entity.MovementReason;
import com.vegas.sistema_gestion_operativa.inventory.raw_material.domain.entity.RawMaterialBatch;
import com.vegas.sistema_gestion_operativa.inventory.raw_material.domain.entity.RawMaterialInventory;
import com.vegas.sistema_gestion_operativa.inventory.raw_material.domain.entity.RawMaterialMovement;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class RawMaterialInventoryFactory {

    public RawMaterialInventory createFromDto(RegisterRawMaterialDto dto){
        return RawMaterialInventory.builder()
                .rawMaterialId(dto.rawMaterialId())
                .branchId(dto.branchId())
                .currentStock(new Quantity(dto.quantity()))
                .averageCost(new Money(0.0))
                .build();
    }

    public RawMaterialBatch createBatchFromDto(RegisterRawMaterialBatchDto dto){
        return RawMaterialBatch.builder()
                .rawMaterialId(dto.rawMaterialId())
                .quantity(new Quantity(dto.quantity()))
                .totalCost(new Money(dto.totalCost()))
                .entryDate(LocalDateTime.now())
                .expirationDate(dto.expirationDate())
                .providerId(dto.providerId())
                .branchId(dto.branchId())
                .batchNumber(dto.batchNumber())
                .build();
    }

    public RawMaterialMovement createMovementFromDto(RegisterRawMaterialBatchDto dto, Long batchId, String userId) {
        return RawMaterialMovement.builder()
                .movementReason(MovementReason.ENTRY)
                .rawMaterialBatchId(batchId)
                .userId(userId)
                .movementDate(LocalDateTime.now())
                .quantity(dto.quantity())
                .build();

    }

}
