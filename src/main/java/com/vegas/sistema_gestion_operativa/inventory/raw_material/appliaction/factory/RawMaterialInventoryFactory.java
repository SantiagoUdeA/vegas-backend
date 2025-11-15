package com.vegas.sistema_gestion_operativa.inventory.raw_material.appliaction.factory;

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
                .currentStock(dto.quantity())
                .build();
    }

    public RawMaterialBatch createBatchFromDto(RegisterRawMaterialBatchDto dto){
        return RawMaterialBatch.builder()
                .rawMaterialId(dto.rawMaterialId())
                .quantity(dto.quantity())
                .entryDate(LocalDateTime.now())
                .expirationDate(dto.expirationDate())
                .providerId(dto.providerId())
                .branchId(dto.branchId())
                .batchNumber(dto.batchNumber())
                .build();
    }

    public RawMaterialMovement createMovementFromDto(RegisterRawMaterialBatchDto dto, Long batchId, String userId) {
        return RawMaterialMovement.builder()
                .movementReason(dto.movementReason())
                .rawMaterialBatchId(batchId)
                .userId(userId)
                .unitOfMeasure(dto.unitOfMeasure())
                .movementDate(LocalDateTime.now())
                .quantity(dto.quantity())
                .build();

    }

}
