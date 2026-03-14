package com.vegas.sistema_gestion_operativa.franchise.domain.exception;

import com.vegas.sistema_gestion_operativa.common.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class FranchiseNameAlreadyExistsException extends ApiException {
    public FranchiseNameAlreadyExistsException(String name) {
        super("Ya existe una franquicia con el nombre: " + name, HttpStatus.CONFLICT);
    }
}
