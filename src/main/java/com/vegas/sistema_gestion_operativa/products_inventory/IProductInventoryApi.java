package com.vegas.sistema_gestion_operativa.products_inventory;

import com.vegas.sistema_gestion_operativa.common.exceptions.AccessDeniedException;
import com.vegas.sistema_gestion_operativa.products_inventory.domain.exceptions.ProductInventoryNotFoundException;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.exceptions.NotEnoughStockException;

public interface IProductInventoryApi {

    /**
     * Consume stock of a product in a specific branch.
     *
     * @param branchId  ID of the branch where the stock will be consumed.
     * @param productId ID of the product whose stock will be consumed.
     * @param quantity  Quantity of stock to consume.
     * @param userId    ID of the user performing the operation.
     * @throws AccessDeniedException             if the user does not have permission to perform this operation.
     * @throws NotEnoughStockException           if there is not enough stock to fulfill the request.
     * @throws ProductInventoryNotFoundException if the product inventory record is not found.
     */
    void consumeProductStock(Long branchId, Long productId, int quantity, String userId) throws AccessDeniedException, NotEnoughStockException, ProductInventoryNotFoundException;

    /**
     * Restore stock of a product in a specific branch.
     *
     * @param branchId  ID of the branch where the stock will be restored.
     * @param productId ID of the product whose stock will be restored.
     * @param quantity  Quantity of stock to restore.
     * @param userId    ID of the user performing the operation.
     * @throws AccessDeniedException             if the user does not have permission to perform this operation.
     * @throws ProductInventoryNotFoundException if the product inventory record is not found.
     */
    void restoreProductStock(Long branchId, Long productId, int quantity, String userId) throws AccessDeniedException, ProductInventoryNotFoundException;
}