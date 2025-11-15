package com.vegas.sistema_gestion_operativa.inventory.raw_material.appliaction.service;

import com.vegas.sistema_gestion_operativa.branches.IBranchApi;
import com.vegas.sistema_gestion_operativa.inventory.raw_material.appliaction.dto.RawMaterialInventoryItemDto;
import com.vegas.sistema_gestion_operativa.inventory.raw_material.appliaction.dto.RegisterRawMaterialBatchDto;
import com.vegas.sistema_gestion_operativa.inventory.raw_material.appliaction.dto.RegisterRawMaterialDto;
import com.vegas.sistema_gestion_operativa.inventory.raw_material.appliaction.factory.RawMaterialInventoryFactory;
import com.vegas.sistema_gestion_operativa.inventory.raw_material.domain.entity.RawMaterialBatch;
import com.vegas.sistema_gestion_operativa.inventory.raw_material.domain.entity.RawMaterialInventory;
import com.vegas.sistema_gestion_operativa.inventory.raw_material.domain.repository.IRawMateriaBatchRepository;
import com.vegas.sistema_gestion_operativa.inventory.raw_material.domain.repository.IRawMaterialInventoryRepository;
import com.vegas.sistema_gestion_operativa.inventory.raw_material.domain.repository.IRawMaterialMovementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RawMaterialInventoryService {

    private final IRawMaterialInventoryRepository rawMaterialInventoryRepository;
    private final RawMaterialInventoryFactory rawMaterialFactory;
    private final IBranchApi branchApi;
    private final IRawMateriaBatchRepository rawMateriaBatchRepository;
    private final IRawMaterialMovementRepository rawMaterialMovementRepository;

    @Autowired
    public RawMaterialInventoryService(IRawMaterialInventoryRepository rawMaterialInventoryRepository, RawMaterialInventoryFactory rawMaterialFactory, IBranchApi branchApi, IRawMateriaBatchRepository rawMateriaBatchRepository, IRawMaterialMovementRepository rawMaterialMovementRepository) {
        this.rawMaterialInventoryRepository = rawMaterialInventoryRepository;
        this.rawMaterialFactory = rawMaterialFactory;
        this.branchApi = branchApi;
        this.rawMateriaBatchRepository = rawMateriaBatchRepository;
        this.rawMaterialMovementRepository = rawMaterialMovementRepository;
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

    public void registerRawMaterialBatch(RegisterRawMaterialBatchDto dto, String userId){
        // Crear un lote de materia prima
        RawMaterialBatch rawMaterialBatch = this.rawMateriaBatchRepository.save(
                this.rawMaterialFactory.createBatchFromDto(dto)
        );
        // Registrar el movimiento del lote de materia prima
        rawMaterialMovementRepository.save(
            this.rawMaterialFactory.createMovementFromDto(
                    dto,
                    rawMaterialBatch.getId(),
                    userId
            )
        );
        // Actualizar inventario y precio promedio

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
    public List<RawMaterialInventoryItemDto> getInventoryByBranchId(Long branchId, String userId) throws com.vegas.sistema_gestion_operativa.common.exceptions.AccessDeniedException {
        branchApi.assertUserHasAccessToBranch(userId, branchId);
        return rawMaterialInventoryRepository.findInventoryItemsByBranchId(branchId);
    }
}
