package com.vegas.sistema_gestion_operativa.inventory.raw_material.appliaction.service;

import com.vegas.sistema_gestion_operativa.branches.IBranchApi;
import com.vegas.sistema_gestion_operativa.common.domain.Money;
import com.vegas.sistema_gestion_operativa.common.domain.Quantity;
import com.vegas.sistema_gestion_operativa.common.exceptions.AccessDeniedException;
import com.vegas.sistema_gestion_operativa.inventory.raw_material.appliaction.dto.RawMaterialBatchDto;
import com.vegas.sistema_gestion_operativa.inventory.raw_material.appliaction.dto.RawMaterialInventoryItemDto;
import com.vegas.sistema_gestion_operativa.inventory.raw_material.appliaction.dto.RegisterRawMaterialBatchDto;
import com.vegas.sistema_gestion_operativa.inventory.raw_material.appliaction.dto.RegisterRawMaterialDto;
import com.vegas.sistema_gestion_operativa.inventory.raw_material.appliaction.factory.RawMaterialInventoryFactory;
import com.vegas.sistema_gestion_operativa.inventory.raw_material.domain.entity.RawMaterialBatch;
import com.vegas.sistema_gestion_operativa.inventory.raw_material.domain.repository.IRawMateriaBatchRepository;
import com.vegas.sistema_gestion_operativa.inventory.raw_material.domain.repository.IRawMaterialInventoryRepository;
import com.vegas.sistema_gestion_operativa.inventory.raw_material.domain.repository.IRawMaterialMovementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
     * Registra un nuevo lote de materia prima en el inventario.
     * Actualiza el stock y el costo promedio en el inventario de la materia prima correspondiente.
     * El costo promedio utiliza la fórmula del promedio ponderado.
     * Registra el movimiento asociado al lote de materia prima.
     *
     * @param dto    Datos del lote de materia prima a registrar
     * @param userId ID del usuario que realiza la operación
     * @return El lote de materia prima registrado
     */
    @Transactional
    public RawMaterialBatch registerRawMaterialBatch(RegisterRawMaterialBatchDto dto, String userId){
        // Obtener unidad de medida de la materia prima


        // Crear un lote de materia prima
        RawMaterialBatch rawMaterialBatch = this.rawMateriaBatchRepository.save(
                this.rawMaterialFactory.createBatchFromDto(dto)
        );

        // Calcular costo unitario de entrada
        Money unitCost = new Money(
                BigDecimal.valueOf(dto.totalCost())
                        .divide(BigDecimal.valueOf(dto.quantity()), 4, RoundingMode.HALF_UP)
        );

        // Obtener o crear inventario
        var inventory = rawMaterialInventoryRepository
                .findByRawMaterialIdAndBranchId(dto.rawMaterialId(), dto.branchId())
                .orElseGet(() -> rawMaterialFactory.createFromDto(
                        new RegisterRawMaterialDto(
                                dto.rawMaterialId(),
                                dto.branchId(),
                                0.0
                        )
                ));

        // Actualizar stock y costo promedio
        Quantity entryQuantity = new Quantity(BigDecimal.valueOf(dto.quantity()));
        inventory.addStock(entryQuantity, unitCost);

        rawMaterialInventoryRepository.save(inventory);

        // Registrar el movimiento del lote de materia prima
        var test = this.rawMaterialFactory.createMovementFromDto(
                dto,
                rawMaterialBatch.getId(),
                userId
        );
        rawMaterialMovementRepository.save(
                test
        );

        return rawMaterialBatch;
    }

    public Page<RawMaterialBatchDto> findRawMaterialBatchesByBranchId(Long branchId, Pageable pageable, String userId) throws AccessDeniedException {
        branchApi.assertUserHasAccessToBranch(
                userId,
                branchId
        );
        return rawMateriaBatchRepository.findByBranchId(branchId, pageable);
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
