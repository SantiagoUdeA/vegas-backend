package com.vegas.sistema_gestion_operativa.raw_material_inventory;

import com.vegas.sistema_gestion_operativa.common.domain.Quantity;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.exceptions.NotEnoughRawMaterialStockException;

import java.util.Map;

public interface IRawMaterialInventoryApi {

    void reduceStock(Map<Long, Quantity> rawMaterialQuantities, String userId) throws NotEnoughRawMaterialStockException;

    void increaseStock(Map<Long, Quantity> rawMaterialQuantities, String userId);
}
