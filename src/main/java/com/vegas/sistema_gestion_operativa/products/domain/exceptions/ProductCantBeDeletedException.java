package com.vegas.sistema_gestion_operativa.products.domain.exceptions;

import com.vegas.sistema_gestion_operativa.common.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class ProductCantBeDeletedException extends ApiException {
    public ProductCantBeDeletedException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
