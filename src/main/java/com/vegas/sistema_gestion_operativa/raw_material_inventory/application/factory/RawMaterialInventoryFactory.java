package com.vegas.sistema_gestion_operativa.raw_material_inventory.application.factory;

import com.vegas.sistema_gestion_operativa.common.domain.Money;
import com.vegas.sistema_gestion_operativa.common.domain.Quantity;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.application.dto.RegisterRawMaterialBatchDto;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.application.dto.RegisterRawMaterialDto;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.entity.RawMaterialBatch;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.entity.RawMaterialInventory;
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

}
