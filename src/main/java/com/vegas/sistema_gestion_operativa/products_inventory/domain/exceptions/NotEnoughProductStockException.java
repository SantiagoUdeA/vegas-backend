package com.vegas.sistema_gestion_operativa.products_inventory.domain.exceptions;

import com.vegas.sistema_gestion_operativa.common.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class NotEnoughProductStockException extends ApiException {
    public NotEnoughProductStockException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}

