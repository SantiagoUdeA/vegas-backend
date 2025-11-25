package com.vegas.sistema_gestion_operativa.products_inventory.application.service;

import com.vegas.sistema_gestion_operativa.branches.IBranchApi;
import com.vegas.sistema_gestion_operativa.common.domain.Quantity;
import com.vegas.sistema_gestion_operativa.common.exceptions.AccessDeniedException;
import com.vegas.sistema_gestion_operativa.common.exceptions.ApiException;
import com.vegas.sistema_gestion_operativa.products.api.IProductApi;
import com.vegas.sistema_gestion_operativa.products.domain.exceptions.ProductNotFoundException;
import com.vegas.sistema_gestion_operativa.products_inventory.IProductInventoryApi;
import com.vegas.sistema_gestion_operativa.products_inventory.application.dto.ProductAdjustmentDto;
import com.vegas.sistema_gestion_operativa.products_inventory.application.dto.ProductInventoryItemDto;
import com.vegas.sistema_gestion_operativa.products_inventory.application.dto.ProductInventoryResponseDto;
import com.vegas.sistema_gestion_operativa.products_inventory.application.dto.RegisterProductStockDto;
import com.vegas.sistema_gestion_operativa.products_inventory.application.factory.ProductInventoryFactory;
import com.vegas.sistema_gestion_operativa.products_inventory.application.factory.ProductInventoryMovementFactory;
import com.vegas.sistema_gestion_operativa.products_inventory.application.mapper.ProductInventoryMapper;
import com.vegas.sistema_gestion_operativa.products_inventory.domain.entity.ProductInventory;
import com.vegas.sistema_gestion_operativa.products_inventory.domain.exceptions.InsufficientStockException;
import com.vegas.sistema_gestion_operativa.products_inventory.domain.exceptions.ProductInventoryNotFoundException;
import com.vegas.sistema_gestion_operativa.products_inventory.domain.repository.IProductInventoryMovementRepository;
import com.vegas.sistema_gestion_operativa.products_inventory.domain.repository.IProductInventoryRepository;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.IRawMaterialInventoryApi;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.exceptions.NotEnoughStockException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductInventoryService implements IProductInventoryApi {

    /**
     * Service that manages product inventory, coordinating access validations,
     * movement persistence and synchronization with raw material inventories.
     */
    private final IProductInventoryRepository productInventoryRepository;
    private final ProductInventoryFactory productInventoryFactory;
    private final ProductInventoryMapper productInventoryMapper;
    private final IBranchApi branchApi;
    private final IProductApi productApi;
    private final IRawMaterialInventoryApi rawMaterialInventoryApi;
    private final ProductInventoryMovementFactory productInventoryMovementFactory;
    private final IProductInventoryMovementRepository productInventoryMovementRepository;

    /**
     * Retrieves a paginated list of inventory items for a given branch after
     * validating the user's access rights.
     *
     * @param branchId branch identifier
     * @param pageable paging configuration
     * @param userId   requesting user identifier
     * @return page containing the inventory items
     * @throws AccessDeniedException when the user cannot access the branch
     */
    public Page<ProductInventoryItemDto> getInventoryByBranchId(Long branchId, Pageable pageable, String userId)
            throws AccessDeniedException {
        branchApi.assertUserHasAccessToBranch(userId, branchId);
        return productInventoryRepository.findInventoryItemsByBranchId(branchId, pageable);
    }

    /**
     * Registers an incoming stock entry and, if the product has a recipe,
     * automatically deducts the corresponding raw materials.
     *
     * @param dto    stock entry payload
     * @param userId user performing the operation
     * @return updated product inventory state
     * @throws ProductNotFoundException when the product does not exist
     * @throws ApiException             when ingredient or stock processing fails
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
     * Performs an inventory adjustment, ensuring the user can operate over the
     * branch, reducing stock and registering the corresponding movement.
     *
     * @param dto    adjustment payload
     * @param userId user executing the adjustment
     * @return updated inventory item
     * @throws InsufficientStockException        when available stock is insufficient
     * @throws ProductInventoryNotFoundException when the inventory item does not exist
     * @throws AccessDeniedException             when the user lacks branch access
     */
    @Transactional
    public ProductInventory doAdjustment(ProductAdjustmentDto dto, String userId)
            throws InsufficientStockException, ProductInventoryNotFoundException, AccessDeniedException {

        // Obtener el item de inventario
        var item = findInventoryItemByProductIdOrThrow(dto.productId());

        // Verificar acceso a la sede
        this.branchApi.assertUserHasAccessToBranch(userId, item.getBranchId());

        // Reducir stock
        item.reduceStock(dto.quantity());

        // Registrar movimiento
        productInventoryMovementRepository.save(
                productInventoryMovementFactory.createMovementForAdjustment(
                        item.getProductId(),
                        dto.quantity(),
                        dto.movementReason(),
                        userId,
                        dto.justification()
                )
        );

        // Guardar cambios
        return productInventoryRepository.save(item);
    }

    /**
     * Handles stock consumption (e.g., sale), validating permissions, checking
     * availability and registering an outgoing movement.
     *
     * @param branchId  branch identifier
     * @param productId product identifier
     * @param quantity  units to consume
     * @param userId    user performing the operation
     * @throws AccessDeniedException             if the user cannot operate on the branch
     * @throws NotEnoughStockException           if there is not enough stock
     * @throws ProductInventoryNotFoundException if the product has no inventory record
     */
    @Transactional
    public void consumeProductStock(Long branchId, Long productId, int quantity, String userId)
            throws AccessDeniedException, NotEnoughStockException, ProductInventoryNotFoundException {

        branchApi.assertUserHasAccessToBranch(userId, branchId);

        ProductInventory inventory = productInventoryRepository
                .findByProductId(productId)
                .orElseThrow(() -> new ProductInventoryNotFoundException("No existe inventario para el producto " + productId)
                );

        Quantity q = new Quantity(quantity);

        if (inventory.getCurrentStock().isLessThan(q)) {
            throw new NotEnoughStockException("Stock insuficiente para el producto " + productId);
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

    /**
     * Restores stock for a product (e.g., a cancelled sale) and records the
     * movement as an entry.
     *
     * @param branchId  branch identifier
     * @param productId product identifier
     * @param quantity  units to restore
     * @param userId    user responsible for the action
     * @throws AccessDeniedException             if the user lacks branch permissions
     * @throws ProductInventoryNotFoundException if the product inventory does not exist
     */
    @Transactional
    public void restoreProductStock(Long branchId, Long productId, int quantity, String userId)
            throws AccessDeniedException, ProductInventoryNotFoundException {

        branchApi.assertUserHasAccessToBranch(userId, branchId);

        ProductInventory inventory = this.findInventoryItemByProductIdOrThrow(productId);

        // Reponer stock
        inventory.addStock(new Quantity(quantity));
        productInventoryRepository.save(inventory);

        // Registrar movimiento (ENTRADA por anulación de venta)
        RegisterProductStockDto movementDto = new RegisterProductStockDto(
                branchId,
                productId,
                quantity,
                inventory.getAverageCost()
        );

        productInventoryMovementRepository.save(
                productInventoryMovementFactory.createReturnEntryMovement(movementDto, userId)
        );
    }

    /**
     * Looks up an inventory item by product id or raises an exception if missing.
     *
     * @param productId product identifier
     * @return inventory entry for the product
     * @throws ProductInventoryNotFoundException when the inventory item cannot be found
     */
    private ProductInventory findInventoryItemByProductIdOrThrow(Long productId) throws ProductInventoryNotFoundException {
        return this.productInventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ProductInventoryNotFoundException(
                        "No se ha encontrado el item de inventario para el producto con id: " + productId
                ));
    }
}
