package com.vegas.sistema_gestion_operativa.products_inventory.application.service;

import com.vegas.sistema_gestion_operativa.branches.IBranchApi;
import com.vegas.sistema_gestion_operativa.common.domain.Quantity;
import com.vegas.sistema_gestion_operativa.common.exceptions.AccessDeniedException;
import com.vegas.sistema_gestion_operativa.common.exceptions.ApiException;
import com.vegas.sistema_gestion_operativa.products.api.IProductApi;
import com.vegas.sistema_gestion_operativa.products.domain.exceptions.ProductNotFoundException;
import com.vegas.sistema_gestion_operativa.products_inventory.application.dto.ProductInventoryItemDto;
import com.vegas.sistema_gestion_operativa.products_inventory.application.dto.ProductInventoryResponseDto;
import com.vegas.sistema_gestion_operativa.products_inventory.application.dto.RegisterProductStockDto;
import com.vegas.sistema_gestion_operativa.products_inventory.application.factory.ProductInventoryFactory;
import com.vegas.sistema_gestion_operativa.products_inventory.application.factory.ProductInventoryMovementFactory;
import com.vegas.sistema_gestion_operativa.products_inventory.application.mapper.ProductInventoryMapper;
import com.vegas.sistema_gestion_operativa.products_inventory.domain.entity.ProductInventory;
import com.vegas.sistema_gestion_operativa.products_inventory.domain.repository.IProductInventoryMovementRepository;
import com.vegas.sistema_gestion_operativa.products_inventory.domain.repository.IProductInventoryRepository;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.IRawMaterialInventoryApi;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductInventoryService {

    private final IProductInventoryRepository productInventoryRepository;
    private final ProductInventoryFactory productInventoryFactory;
    private final ProductInventoryMapper productInventoryMapper;
    private final IBranchApi branchApi;
    private final IProductApi productApi;
    private final IRawMaterialInventoryApi rawMaterialInventoryApi;
    private final ProductInventoryMovementFactory productInventoryMovementFactory;
    private final IProductInventoryMovementRepository productInventoryMovementRepository;

    /**
     * Obtener inventario de productos por sede
     */
    public Page<ProductInventoryItemDto> getInventoryByBranchId(Long branchId, Pageable pageable, String userId)
            throws AccessDeniedException {
        branchApi.assertUserHasAccessToBranch(userId, branchId);
        return productInventoryRepository.findInventoryItemsByBranchId(branchId, pageable);
    }

    /**
     * Registrar entradas de stock (compras, ajustes, etc.)
     */
    @Transactional
    public ProductInventoryResponseDto registerProductStock(RegisterProductStockDto dto, String userId)
            throws ProductNotFoundException, ApiException {

        branchApi.assertUserHasAccessToBranch(userId, dto.branchId());

        ProductInventory inventory = productInventoryRepository
                .findByProductId(dto.productId())
                .orElseGet(() -> productInventoryFactory.createFromDto(dto));

        Quantity entryQuantity = new Quantity(dto.quantity());
        inventory.addStock(entryQuantity);

        var recipe = productApi.getIngredientsForProductUnit(dto.productId());

        if (recipe.isPresent()) {
            Map<Long, Quantity> cantidadesAReducir = new HashMap<>();
            recipe.get().forEach(ingredient ->
                    cantidadesAReducir.put(
                            ingredient.getRawMaterialId(),
                            ingredient.getQuantity().multiply(entryQuantity)
                    )
            );

            rawMaterialInventoryApi.reduceStock(cantidadesAReducir, userId);
        }

        ProductInventory saved = productInventoryRepository.save(inventory);

        productInventoryMovementRepository.save(
                productInventoryMovementFactory.createEntryMovement(dto, userId)
        );

        return productInventoryMapper.toResponseDto(saved);
    }

    /**
     * 🔥 Consumir stock cuando se realiza una venta
     */
    @Transactional
    public void consumeProductStock(Long branchId, Long productId, int quantity, String userId)
            throws ApiException {

        branchApi.assertUserHasAccessToBranch(userId, branchId);

        ProductInventory inventory = productInventoryRepository
                .findByProductId(productId)
                .orElseThrow(() -> new ApiException("No existe inventario para el producto " + productId,
                        HttpStatus.BAD_REQUEST)
                );

        Quantity q = new Quantity(quantity);

        if (inventory.getCurrentStock().isLessThan(q)) {
            throw new ApiException("Stock insuficiente para el producto " + productId,
            HttpStatus.BAD_REQUEST);
        }

        // Descontar
        inventory.subtractStock(q);
        productInventoryRepository.save(inventory);

        // Registrar movimiento (SALIDA)
        RegisterProductStockDto movementDto = new RegisterProductStockDto(
                branchId,
                productId,
                quantity,
                inventory.getAverageCost()
        );

        productInventoryMovementRepository.save(
                productInventoryMovementFactory.createSaleMovement(movementDto, userId)
        );
    }

    @Transactional
    public void restoreProductStock(Long branchId, Long productId, int quantity, String userId)
            throws ApiException {

        branchApi.assertUserHasAccessToBranch(userId, branchId);

        ProductInventory inventory = productInventoryRepository
                .findByProductId(productId)
                .orElseThrow(() -> new ApiException(
                        "No existe inventario para el producto " + productId,
                        HttpStatus.NOT_FOUND
                ));

        Quantity q = new Quantity(quantity);

        // Reponer stock
        inventory.addStock(q);
        productInventoryRepository.save(inventory);

        // Registrar movimiento (ENTRADA por anulación de venta)
        RegisterProductStockDto movementDto = new RegisterProductStockDto(
                branchId,
                productId,
                quantity,
                inventory.getAverageCost()
        );

        productInventoryMovementRepository.save(
                productInventoryMovementFactory.createEntryMovement(movementDto, userId)
        );
    }
}
