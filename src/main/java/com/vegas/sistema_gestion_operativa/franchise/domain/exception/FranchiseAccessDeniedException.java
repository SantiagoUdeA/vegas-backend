package com.vegas.sistema_gestion_operativa.franchise.domain.exception;

import com.vegas.sistema_gestion_operativa.common.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class FranchiseAccessDeniedException extends ApiException {
    public FranchiseAccessDeniedException(Long franchiseId) {
        super("No tiene permisos para acceder a la franquicia con ID " + franchiseId + ".", HttpStatus.FORBIDDEN);
    }
}
