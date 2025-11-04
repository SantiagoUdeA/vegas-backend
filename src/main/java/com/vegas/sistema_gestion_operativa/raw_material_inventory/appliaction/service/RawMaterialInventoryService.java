package com.vegas.sistema_gestion_operativa.raw_material_inventory.appliaction.service;

import com.vegas.sistema_gestion_operativa.raw_material_inventory.appliaction.dto.RegisterRawMaterialDto;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.appliaction.factory.RawMaterialInventoryFactory;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.entity.RawMaterialInventory;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.repository.IRawMaterialInventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RawMaterialInventoryService {

    private final IRawMaterialInventoryRepository rawMaterialInventoryRepository;
    private final RawMaterialInventoryFactory rawMaterialFactory;

    @Autowired
    public RawMaterialInventoryService(IRawMaterialInventoryRepository rawMaterialInventoryRepository, RawMaterialInventoryFactory rawMaterialFactory) {
        this.rawMaterialInventoryRepository = rawMaterialInventoryRepository;
        this.rawMaterialFactory = rawMaterialFactory;
    }

    public RawMaterialInventory registerRawMaterial(RegisterRawMaterialDto dto) {
        var rawMaterialInventory = rawMaterialFactory.createFromDto(dto);
        return rawMaterialInventoryRepository.save(rawMaterialInventory);
    }
}
