package com.vegas.sistema_gestion_operativa.catalog.raw_material.domain.exceptions;

import com.vegas.sistema_gestion_operativa.common.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class RawMaterialNameAlreadyExists extends ApiException {
    public RawMaterialNameAlreadyExists(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
