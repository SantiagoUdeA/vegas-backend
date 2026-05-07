package com.vegas.sistema_gestion_operativa.production.application.service;

import com.vegas.sistema_gestion_operativa.branches.IBranchApi;
import com.vegas.sistema_gestion_operativa.common.domain.Quantity;
import com.vegas.sistema_gestion_operativa.common.domain.MovementReason;
import com.vegas.sistema_gestion_operativa.common.exceptions.ApiException;
import com.vegas.sistema_gestion_operativa.common.exceptions.BadRequestException;
import com.vegas.sistema_gestion_operativa.products.api.IProductApi;
import com.vegas.sistema_gestion_operativa.products.api.IngredientDto;
import com.vegas.sistema_gestion_operativa.products.api.ProductDto;
import com.vegas.sistema_gestion_operativa.production.application.dto.*;
import com.vegas.sistema_gestion_operativa.production.domain.entity.Production;
import com.vegas.sistema_gestion_operativa.production.domain.entity.ProductionBatchConsumption;
import com.vegas.sistema_gestion_operativa.production.domain.exceptions.InsufficientRawMaterialException;
import com.vegas.sistema_gestion_operativa.production.domain.repository.IProductionBatchConsumptionRepository;
import com.vegas.sistema_gestion_operativa.production.domain.repository.IProductionRepository;
import com.vegas.sistema_gestion_operativa.products_inventory.domain.entity.ProductInventory;
import com.vegas.sistema_gestion_operativa.products_inventory.domain.entity.ProductInventoryMovement;
import com.vegas.sistema_gestion_operativa.products_inventory.domain.repository.IProductInventoryMovementRepository;
import com.vegas.sistema_gestion_operativa.products_inventory.domain.repository.IProductInventoryRepository;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.application.factory.RawMaterialMovementFactory;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.entity.RawMaterialBatch;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.entity.RawMaterialInventory;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.repository.IRawMateriaBatchRepository;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.repository.IRawMaterialInventoryRepository;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.repository.IRawMaterialMovementRepository;
import com.vegas.sistema_gestion_operativa.common.utils.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ProductionService {

    private final IProductionRepository productionRepository;
    private final IProductionBatchConsumptionRepository batchConsumptionRepository;
    private final IBranchApi branchApi;
    private final IProductApi productApi;
    private final IRawMateriaBatchRepository rawMaterialBatchRepository;
    private final IRawMaterialInventoryRepository rawMaterialInventoryRepository;
    private final IRawMaterialMovementRepository rawMaterialMovementRepository;
    private final RawMaterialMovementFactory rawMaterialMovementFactory;
    private final IProductInventoryRepository productInventoryRepository;
    private final IProductInventoryMovementRepository productInventoryMovementRepository;

    @Autowired
    public ProductionService(
            IProductionRepository productionRepository,
            IProductionBatchConsumptionRepository batchConsumptionRepository,
            IBranchApi branchApi,
            IProductApi productApi,
            IRawMateriaBatchRepository rawMaterialBatchRepository,
            IRawMaterialInventoryRepository rawMaterialInventoryRepository,
            IRawMaterialMovementRepository rawMaterialMovementRepository,
            RawMaterialMovementFactory rawMaterialMovementFactory,
            IProductInventoryRepository productInventoryRepository,
            IProductInventoryMovementRepository productInventoryMovementRepository
    ) {
        this.productionRepository = productionRepository;
        this.batchConsumptionRepository = batchConsumptionRepository;
        this.branchApi = branchApi;
        this.productApi = productApi;
        this.rawMaterialBatchRepository = rawMaterialBatchRepository;
        this.rawMaterialInventoryRepository = rawMaterialInventoryRepository;
        this.rawMaterialMovementRepository = rawMaterialMovementRepository;
        this.rawMaterialMovementFactory = rawMaterialMovementFactory;
        this.productInventoryRepository = productInventoryRepository;
        this.productInventoryMovementRepository = productInventoryMovementRepository;
    }

    @Transactional
    public ProductionResponseDto registerProduction(RegisterProductionDto dto, String userId) throws ApiException {
        // Step 1: Validations
        branchApi.assertUserHasAccessToBranch(userId, dto.branchId());

        ProductDto product = productApi.getProductByIdOrThrow(dto.productId());
        productApi.assertProductBelongsToBranch(dto.productId(), dto.branchId());

        var recipe = product.getRecipe();
        if (recipe == null || recipe.getActive() == null || !recipe.getActive()) {
            throw new BadRequestException("El producto no tiene una receta activa.");
        }

        List<IngredientDto> ingredients = productApi.getIngredientsForProductUnit(dto.productId())
                .orElseThrow(() -> new BadRequestException("El producto no tiene receta activa."));

        if (ingredients.isEmpty()) {
            throw new BadRequestException("La receta del producto no tiene ingredientes.");
        }

        BigDecimal qty = dto.quantityToProduce();

        // Step 2: Calculate required quantities (ingredients already scaled per unit)
        Map<Long, BigDecimal> requiredByRawMaterial = new LinkedHashMap<>();
        Map<Long, IngredientDto> ingredientByRawMaterial = new LinkedHashMap<>();
        for (IngredientDto ingredient : ingredients) {
            BigDecimal required = ingredient.getQuantity().getValue().multiply(qty);
            requiredByRawMaterial.put(ingredient.getRawMaterialId(), required);
            ingredientByRawMaterial.put(ingredient.getRawMaterialId(), ingredient);
        }

        // Step 3: Fail-fast MP validation
        List<RawMaterialShortageDto> shortages = new ArrayList<>();
        for (Map.Entry<Long, BigDecimal> entry : requiredByRawMaterial.entrySet()) {
            Long rawMaterialId = entry.getKey();
            BigDecimal required = entry.getValue();
            BigDecimal available = rawMaterialBatchRepository.sumAvailableByRawMaterialAndBranch(rawMaterialId, dto.branchId());
            if (available.compareTo(required) < 0) {
                IngredientDto ingredient = ingredientByRawMaterial.get(rawMaterialId);
                shortages.add(RawMaterialShortageDto.builder()
                        .rawMaterialId(rawMaterialId)
                        .rawMaterialName(ingredient.getRawMaterialName())
                        .unitOfMeasure(ingredient.getRawMaterialUnitOfMeasure())
                        .required(required)
                        .available(available)
                        .deficit(required.subtract(available))
                        .build());
            }
        }
        if (!shortages.isEmpty()) {
            throw new InsufficientRawMaterialException(shortages);
        }

        // Step 4: Create Production record first to get ID
        Production production = productionRepository.save(Production.builder()
                .productId(dto.productId())
                .recipeId(recipe.getId())
                .quantityProduced(new Quantity(qty))
                .branchId(dto.branchId())
                .userId(userId)
                .observations(dto.observations())
                .productionDate(DateTimeUtils.nowInBogota())
                .build());

        // Step 5: Consume MP per batch (FIFO)
        List<RawMaterialConsumptionDetailDto> consumptionDetails = new ArrayList<>();

        for (Map.Entry<Long, BigDecimal> entry : requiredByRawMaterial.entrySet()) {
            Long rawMaterialId = entry.getKey();
            BigDecimal totalRequired = entry.getValue();
            IngredientDto ingredient = ingredientByRawMaterial.get(rawMaterialId);

            List<RawMaterialBatch> batches = rawMaterialBatchRepository.findAvailableBatchesFifo(rawMaterialId, dto.branchId());

            List<BatchConsumptionDetailDto> batchDetails = new ArrayList<>();
            BigDecimal remaining = totalRequired;

            for (RawMaterialBatch batch : batches) {
                if (remaining.compareTo(BigDecimal.ZERO) <= 0) break;

                BigDecimal consume = remaining.min(batch.getAvailableQuantity().getValue());
                Quantity consumeQty = new Quantity(consume);

                batch.setAvailableQuantity(batch.getAvailableQuantity().subtract(consumeQty));
                rawMaterialBatchRepository.save(batch);

                batchConsumptionRepository.save(ProductionBatchConsumption.builder()
                        .productionId(production.getId())
                        .rawMaterialBatchId(batch.getId())
                        .rawMaterialId(rawMaterialId)
                        .quantityConsumed(consumeQty)
                        .build());

                rawMaterialMovementRepository.save(
                        rawMaterialMovementFactory.createProductionMovement(
                                rawMaterialId, batch.getId(), consumeQty, production.getId(), userId
                        )
                );

                batchDetails.add(BatchConsumptionDetailDto.builder()
                        .batchId(batch.getId())
                        .batchNumber(batch.getBatchNumber())
                        .quantityConsumed(consume)
                        .build());

                remaining = remaining.subtract(consume);
            }

            // Reduce global raw material inventory
            RawMaterialInventory inventory = rawMaterialInventoryRepository
                    .findByRawMaterialIdAndBranchId(rawMaterialId, dto.branchId())
                    .orElseThrow(() -> new RuntimeException("Inventario de materia prima no encontrado: " + rawMaterialId));
            inventory.setCurrentStock(inventory.getCurrentStock().subtract(new Quantity(totalRequired)));
            rawMaterialInventoryRepository.save(inventory);

            consumptionDetails.add(RawMaterialConsumptionDetailDto.builder()
                    .rawMaterialId(rawMaterialId)
                    .rawMaterialName(ingredient.getRawMaterialName())
                    .totalConsumed(totalRequired)
                    .batchesConsumed(batchDetails)
                    .build());
        }

        // Step 6: Add PT to product inventory
        ProductInventory productInventory = productInventoryRepository
                .findByProductId(dto.productId())
                .orElseGet(() -> productInventoryRepository.save(
                        ProductInventory.builder()
                                .productId(dto.productId())
                                .branchId(dto.branchId())
                                .currentStock(new Quantity(0.0))
                                .averageCost(0.0)
                                .build()
                ));

        productInventory.addStock(new Quantity(qty));
        productInventoryRepository.save(productInventory);

        productInventoryMovementRepository.save(ProductInventoryMovement.builder()
                .productId(dto.productId())
                .movementReason(MovementReason.PRODUCCION)
                .quantity(new Quantity(qty))
                .userId(userId)
                .movementDate(DateTimeUtils.nowInBogota())
                .build());

        return ProductionResponseDto.builder()
                .productionId(production.getId())
                .productId(dto.productId())
                .productName(product.getName())
                .quantityProduced(qty)
                .productionDate(production.getProductionDate())
                .rawMaterialsConsumed(consumptionDetails)
                .build();
    }
}
