package com.vegas.sistema_gestion_operativa.branches.domain.exception;

import com.vegas.sistema_gestion_operativa.common.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class BranchNameAlreadyExistsException extends ApiException {
    public BranchNameAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
