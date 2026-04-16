package com.vegas.sistema_gestion_operativa.branches.domain.exception;

import com.vegas.sistema_gestion_operativa.common.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class BranchDuplicateInformationException extends ApiException {
    public BranchDuplicateInformationException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
