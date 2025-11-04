package com.vegas.sistema_gestion_operativa.raw_material_inventory.appliaction.factory;

import com.vegas.sistema_gestion_operativa.raw_material_inventory.appliaction.dto.RegisterRawMaterialDto;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.entity.RawMaterialInventory;
import org.springframework.stereotype.Component;

@Component
public class RawMaterialInventoryFactory {

    public RawMaterialInventory createFromDto(RegisterRawMaterialDto dto){
        return RawMaterialInventory.builder()
                .rawMaterialId(dto.rawMaterialId())
                .branchId(dto.branchId())
                .currentStock(0.0)
                .build();
    }
}
