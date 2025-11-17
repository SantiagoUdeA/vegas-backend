package com.vegas.sistema_gestion_operativa.raw_material.domain.exceptions;

import com.vegas.sistema_gestion_operativa.common.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class RawMaterialNotFoundException extends ApiException {
    public RawMaterialNotFoundException(String id) {
        super("La materia prima con id " + id + " no fue encontrada.", HttpStatus.NOT_FOUND);
    }
}

