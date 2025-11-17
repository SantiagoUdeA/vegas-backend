package com.vegas.sistema_gestion_operativa.raw_material.domain.exceptions;

import com.vegas.sistema_gestion_operativa.common.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class RawMaterialCategoryNotFoundException extends ApiException {
    public RawMaterialCategoryNotFoundException(Long categoryId) {
        super("La categoría de materia prima con ID " + categoryId + " no fue encontrada.", HttpStatus.NOT_FOUND);
    }
}

