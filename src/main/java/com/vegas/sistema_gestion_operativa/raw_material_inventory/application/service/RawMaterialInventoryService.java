package com.vegas.sistema_gestion_operativa.raw_material_inventory.application.service;

import com.vegas.sistema_gestion_operativa.branches.IBranchApi;
import com.vegas.sistema_gestion_operativa.common.domain.Money;
import com.vegas.sistema_gestion_operativa.common.domain.MovementReason;
import com.vegas.sistema_gestion_operativa.common.domain.Quantity;
import com.vegas.sistema_gestion_operativa.common.exceptions.AccessDeniedException;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.IRawMaterialInventoryApi;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.application.dto.*;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.application.factory.RawMaterialInventoryFactory;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.application.factory.RawMaterialMovementFactory;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.entity.RawMaterialBatch;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.entity.RawMaterialInventory;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.entity.RawMaterialMovement;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.exceptions.InventoryItemNotFoundException;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.exceptions.NotEnoughRawMaterialStockException;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.repository.IRawMateriaBatchRepository;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.repository.IRawMaterialInventoryRepository;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.repository.IRawMaterialMovementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RawMaterialInventoryService implements IRawMaterialInventoryApi {

    private final IRawMaterialInventoryRepository rawMaterialInventoryRepository;
    private final RawMaterialInventoryFactory rawMaterialFactory;
    private final RawMaterialMovementFactory rawMaterialMovementFactory;
    private final IBranchApi branchApi;
    private final IRawMateriaBatchRepository rawMateriaBatchRepository;
    private final IRawMaterialMovementRepository rawMaterialMovementRepository;

    @Autowired
    public RawMaterialInventoryService(IRawMaterialInventoryRepository rawMaterialInventoryRepository, RawMaterialInventoryFactory rawMaterialFactory, RawMaterialMovementFactory rawMaterialMovementFactory, IBranchApi branchApi, IRawMateriaBatchRepository rawMateriaBatchRepository, IRawMaterialMovementRepository rawMaterialMovementRepository) {
        this.rawMaterialInventoryRepository = rawMaterialInventoryRepository;
        this.rawMaterialFactory = rawMaterialFactory;
        this.rawMaterialMovementFactory = rawMaterialMovementFactory;
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
    public RawMaterialBatch registerRawMaterialBatch(RegisterRawMaterialBatchDto dto, String userId) {
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
        var inventory = getOrCreateInventory(dto.rawMaterialId(), dto.branchId());

        // Actualizar stock y costo promedio
        Quantity entryQuantity = new Quantity(BigDecimal.valueOf(dto.quantity()));
        inventory.addStock(entryQuantity, unitCost);

        rawMaterialInventoryRepository.save(inventory);

        // Registrar el movimiento del lote de materia prima
        var movement = this.rawMaterialMovementFactory.createMovementFromDto(
                dto,
                rawMaterialBatch.getId(),
                userId
        );

        rawMaterialMovementRepository.save(movement);

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

    @Transactional
    public void reduceStock(Map<Long, Quantity> quantities, String userId) throws NotEnoughRawMaterialStockException {
        // 1. Obtener todos los inventarios afectados
        Map<Long, RawMaterialInventory> inventoryMap = loadInventoriesByIds(quantities.keySet());

        // 2. Validar existencia y stock suficiente
        validateItemsExistFor(quantities.keySet(), inventoryMap);

        // 3. Reducir el stock ahora que todo ya está validado
        List<RawMaterialMovement> movements = new ArrayList<>();
        for (Map.Entry<Long, Quantity> entry : quantities.entrySet()) {
            Long rawMaterialId = entry.getKey();
            Quantity quantity = entry.getValue();
            inventoryMap.get(rawMaterialId).reduceStock(quantity);
            movements.add(rawMaterialMovementFactory.createMovementForAdjustment(
                    rawMaterialId,
                    quantity,
                    MovementReason.SALIDA,
                    userId
            ));
        }

        // 4. Guardar la información de inventario y movimientos
        persistInventoryAndMovements(inventoryMap.values(), movements);
    }

    @Override
    public void increaseStock(Map<Long, Quantity> rawMaterialQuantities, String userId, MovementReason reason) {

        // 1. Obtener todos los inventarios afectados
        Map<Long, RawMaterialInventory> inventoryMap = loadInventoriesByIds(rawMaterialQuantities.keySet());

        // 2. Aumentar el stock
        List<RawMaterialMovement> movements = new ArrayList<>();
        for (Map.Entry<Long, Quantity> entry : rawMaterialQuantities.entrySet()) {
            Long rawMaterialId = entry.getKey();
            Quantity quantity = entry.getValue();
            RawMaterialInventory item = inventoryMap.get(rawMaterialId);
            if (item != null) {
                item.addStock(quantity);
                movements.add(rawMaterialMovementFactory.createMovementForAdjustment(
                        rawMaterialId,
                        quantity,
                        reason,
                        userId
                ));
            }
        }

        // 3. Guardar la información de inventario y movimientos
        persistInventoryAndMovements(inventoryMap.values(), movements);
    }

    /**
     * Realiza un ajuste en el inventario de materia prima.
     * Verifica que el usuario tenga acceso a la sede correspondiente.
     * Reduce el stock del inventario según la cantidad especificada en el ajuste.
     * Registra un movimiento de inventario asociado al ajuste.
     *
     * @param dto    Datos del ajuste
     * @param userId ID del usuario que realiza el ajuste
     * @return El item de inventario ajustado
     * @throws NotEnoughRawMaterialStockException si no hay suficiente stock para realizar el ajuste
     * @throws InventoryItemNotFoundException     si no se encuentra el item de inventario
     * @throws AccessDeniedException              si el usuario no tiene acceso a la sede correspondiente
     */
    public RawMaterialInventory doAdjustment(RawMaterialAdjustmentDto dto, String userId) throws NotEnoughRawMaterialStockException, InventoryItemNotFoundException, AccessDeniedException {
        // Obtener el item de inventario
        var item = this.rawMaterialInventoryRepository.findByRawMaterialId((dto.rawMaterialId()))
                .orElseThrow(() -> new InventoryItemNotFoundException(
                        "No se ha encontrado el item de inventario para la materia prima con id: " + dto.rawMaterialId()
                ));

        // Verificar acceso del usuario a la sede
        this.branchApi.assertUserHasAccessToBranch(
                userId,
                item.getBranchId()
        );

        // Realizar el ajuste reduciendo el stock
        item.reduceStock(dto.quantity());

        // Registrar el movimiento asociado al ajuste
        rawMaterialMovementRepository.save(
                rawMaterialMovementFactory.createMovementForAdjustment(
                        item.getRawMaterialId(),
                        dto.quantity(),
                        dto.movementReason(),
                        userId,
                        dto.justification()
                )
        );

        // Guardar y retornar el item ajustado
        return rawMaterialInventoryRepository.save(item);
    }

    // ===================== Métodos privados reutilizables =====================

    private Map<Long, RawMaterialInventory> loadInventoriesByIds(Set<Long> rawMaterialIds) {
        List<RawMaterialInventory> items = rawMaterialInventoryRepository.findByRawMaterialIdIn(new ArrayList<>(rawMaterialIds));
        return items.stream().collect(Collectors.toMap(RawMaterialInventory::getRawMaterialId, i -> i));
    }

    private void validateItemsExistFor(Set<Long> requestedIds, Map<Long, RawMaterialInventory> inventoryMap) throws NotEnoughRawMaterialStockException {
        for (Long id : requestedIds) {
            if (!inventoryMap.containsKey(id)) {
                throw new NotEnoughRawMaterialStockException(
                        // TODO Especificar el nombre de la materia prima
                        "No hay suficiente inventario para una de las materias primas (id: " + id + ")"
                );
            }
        }
    }

    private void persistInventoryAndMovements(Collection<RawMaterialInventory> inventories, List<RawMaterialMovement> movements) {
        rawMaterialMovementRepository.saveAll(movements);
        rawMaterialInventoryRepository.saveAll(inventories);
    }

    private RawMaterialInventory getOrCreateInventory(Long rawMaterialId, Long branchId) {
        return rawMaterialInventoryRepository
                .findByRawMaterialIdAndBranchId(rawMaterialId, branchId)
                .orElseGet(() -> rawMaterialFactory.createFromDto(
                        new RegisterRawMaterialDto(
                                rawMaterialId,
                                branchId,
                                0.0
                        )
                ));
    }
}
