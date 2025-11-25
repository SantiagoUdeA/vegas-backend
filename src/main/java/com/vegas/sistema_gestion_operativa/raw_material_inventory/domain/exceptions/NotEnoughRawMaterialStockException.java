package com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.exceptions;

import com.vegas.sistema_gestion_operativa.common.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class NotEnoughRawMaterialStockException extends ApiException {

    public NotEnoughRawMaterialStockException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
