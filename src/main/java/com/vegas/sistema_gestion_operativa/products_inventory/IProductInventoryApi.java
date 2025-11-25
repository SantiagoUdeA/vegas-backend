package com.vegas.sistema_gestion_operativa.products_inventory;

import com.vegas.sistema_gestion_operativa.common.domain.Quantity;
import com.vegas.sistema_gestion_operativa.common.exceptions.ApiException;

public interface IProductInventoryApi {

    /**
     * Consume stock of a product in a specific branch.
     *
     * @param branchId  ID of the branch where the stock will be consumed.
     * @param productId ID of the product whose stock will be consumed.
     * @param quantity  Quantity of stock to consume.
     * @param userId    ID of the user performing the operation.
     * @throws ApiException if there is an error during the operation.
     */
    void consumeProductStock(Long branchId, Long productId, Quantity quantity, String userId) throws ApiException;

    /**
     * Restore stock of a product in a specific branch.
     *
     * @param branchId  ID of the branch where the stock will be restored.
     * @param productId ID of the product whose stock will be restored.
     * @param quantity  Quantity of stock to restore.
     * @param userId    ID of the user performing the operation.
     * @throws ApiException if there is an error during the operation.
     */
    void restoreProductStock(Long branchId, Long productId, Quantity quantity, String userId) throws ApiException;
}