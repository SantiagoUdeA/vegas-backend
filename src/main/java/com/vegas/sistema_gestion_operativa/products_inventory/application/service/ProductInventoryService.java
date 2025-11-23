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
     * Obtiene el inventario de productos de una sede específica con paginación.
     * Verifica que el usuario tenga acceso a la sede antes de devolver los datos.
     * Incluye información del nombre del producto y su categoría.
     *
     * @param branchId ID de la sede
     * @param pageable Parámetros de paginación (página, tamaño, orden)
     * @param userId   ID del usuario que solicita el inventario
     * @return Página de elementos del inventario de productos con información detallada
     * @throws AccessDeniedException si el usuario no tiene acceso a la sede
     */
    public Page<ProductInventoryItemDto> getInventoryByBranchId(Long branchId, Pageable pageable, String userId)
            throws AccessDeniedException {
        branchApi.assertUserHasAccessToBranch(userId, branchId);
        return productInventoryRepository.findInventoryItemsByBranchId(branchId, pageable);
    }

    /**
     * Registra o actualiza stock de un producto en el inventario.
     * Actualiza el stock y el costo promedio usando promedio ponderado.
     * El branchId se obtiene del producto asociado.
     *
     * @param dto    Datos del stock a registrar
     * @param userId ID del usuario que realiza la operación
     * @return El inventario del producto actualizado
     * @throws ProductNotFoundException si el producto no existe
     * @throws AccessDeniedException si el usuario no tiene acceso a la sede del producto
     */
    @Transactional
    public ProductInventoryResponseDto registerProductStock(RegisterProductStockDto dto, String userId)
            throws ProductNotFoundException, ApiException {

        // Verificar que el usuario tenga acceso a la sede del producto
        branchApi.assertUserHasAccessToBranch(userId, dto.branchId());

        // Obtener o crear inventario
        ProductInventory inventory = productInventoryRepository
                .findByProductId(dto.productId())
                .orElseGet(() -> productInventoryFactory.createFromDto(dto));

        // Actualizar stock
        Quantity entryQuantity = new Quantity(dto.quantity());
        inventory.addStock(entryQuantity);

        // Llamada al inventario de materias primas para reducir el stock si existe una fórmula
        var recipe = productApi.getIngredientsForProductUnit(dto.productId());

        if(recipe.isPresent()){
            Map<Long, Quantity> cantidadesAReducir = new HashMap<>();
            for (var ingredient : recipe.get()) {
                cantidadesAReducir.put(
                        ingredient.getRawMaterialId(),
                        ingredient.getQuantity().multiply(entryQuantity)
                );
            }
            this.rawMaterialInventoryApi.reduceStock(cantidadesAReducir, userId);
        }

        // Guardar  el inventario actualizado
        ProductInventory saved = productInventoryRepository.save(inventory);

        // Registrar movimiento en el inventario de productos
        this.productInventoryMovementRepository.save(
                this.productInventoryMovementFactory.createFromDto(dto, userId)
        );

        return productInventoryMapper.toResponseDto(saved);
    }
}

