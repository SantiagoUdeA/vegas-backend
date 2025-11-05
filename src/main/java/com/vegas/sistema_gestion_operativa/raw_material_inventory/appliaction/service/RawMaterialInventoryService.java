package com.vegas.sistema_gestion_operativa.raw_material_inventory.appliaction.service;

import com.vegas.sistema_gestion_operativa.branches.IBranchApi;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.appliaction.dto.RawMaterialInventoryItemDto;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.appliaction.dto.RegisterRawMaterialDto;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.appliaction.factory.RawMaterialInventoryFactory;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.entity.RawMaterialInventory;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.repository.IRawMaterialInventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RawMaterialInventoryService {

    private final IRawMaterialInventoryRepository rawMaterialInventoryRepository;
    private final RawMaterialInventoryFactory rawMaterialFactory;
    private final IBranchApi branchApi;

    @Autowired
    public RawMaterialInventoryService(IRawMaterialInventoryRepository rawMaterialInventoryRepository, RawMaterialInventoryFactory rawMaterialFactory, IBranchApi branchApi) {
        this.rawMaterialInventoryRepository = rawMaterialInventoryRepository;
        this.rawMaterialFactory = rawMaterialFactory;
        this.branchApi = branchApi;
    }

    /**
     * Registra una materia prima en el inventario de una sede.
     * Si la materia prima ya existe en el inventario de la sede, se incrementa su stock.
     *
     * @param dto Datos necesarios para registrar la materia prima
     * @return La entidad RawMaterialInventory registrada o actualizada
     */
    public RawMaterialInventory registerRawMaterial(RegisterRawMaterialDto dto) {
        var rawMaterialItem = rawMaterialInventoryRepository
                .findByRawMaterialIdAndBranchId(dto.rawMaterialId(), dto.branchId());
        if(rawMaterialItem.isPresent()){
            var existingItem = rawMaterialItem.get();
            existingItem.addStock(dto.quantity());
            return rawMaterialInventoryRepository.save(existingItem);
        }else{
            var rawMaterialInventory = rawMaterialFactory.createFromDto(dto);
            return rawMaterialInventoryRepository.save(rawMaterialInventory);
        }
    }

    /**
     * Obtiene el inventario de materias primas de una sede específica.
     * Verifica que el usuario tenga acceso a la sede antes de devolver los datos.
     *
     * @param branchId ID de la sede
     * @param userId   ID del usuario que solicita el inventario
     * @return Lista de elementos del inventario de materias primas
     * @throws AccessDeniedException si el usuario no tiene acceso a la sede
     */
    public List<RawMaterialInventoryItemDto> getInventoryByBranchId(Long branchId, String userId) {
        var userBranches = branchApi.getUserBranches(userId);
        if(!userBranches.contains(branchId)){
            throw new AccessDeniedException("No puedes acceder al inventario de esta sede");
        }
        return rawMaterialInventoryRepository.findInventoryItemsByBranchId(branchId);
    }
}
