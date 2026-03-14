package com.vegas.sistema_gestion_operativa.franchise.domain.exception;

import com.vegas.sistema_gestion_operativa.common.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class FranchiseNotFoundException extends ApiException {
    public FranchiseNotFoundException(Long franchiseId) {
        super("La franquicia con ID " + franchiseId + " no fue encontrada.", HttpStatus.NOT_FOUND);
    }
}
