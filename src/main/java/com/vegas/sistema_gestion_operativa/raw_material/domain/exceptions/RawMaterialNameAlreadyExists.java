package com.vegas.sistema_gestion_operativa.raw_material.domain.exceptions;

import com.vegas.sistema_gestion_operativa.common.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class RawMaterialNameAlreadyExists extends ApiException {
    public RawMaterialNameAlreadyExists(String name) {
        super("La materia prima con nombre " + name + " ya existe", HttpStatus.CONFLICT);
    }
}
