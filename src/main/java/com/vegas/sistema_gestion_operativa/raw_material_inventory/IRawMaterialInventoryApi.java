package com.vegas.sistema_gestion_operativa.raw_material_inventory;

import com.vegas.sistema_gestion_operativa.common.domain.Quantity;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.exceptions.NotEnoughStockException;

import java.util.Map;

public interface IRawMaterialInventoryApi {

    public void reduceStock(Map<Long, Quantity> rawMaterialQuantities) throws NotEnoughStockException;
}
