package com.vegas.sistema_gestion_operativa.raw_material.domain.exceptions;

import com.vegas.sistema_gestion_operativa.common.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class RawMaterialCategoryNameAlreadyExistsException extends ApiException {
    public RawMaterialCategoryNameAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
