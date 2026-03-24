package com.vegas.sistema_gestion_operativa.subscription.domain.exception;

import com.vegas.sistema_gestion_operativa.common.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class FranchiseLimitExceededException extends ApiException {
    public FranchiseLimitExceededException(int currentCount, int maxAllowed) {
        super(String.format(
                "Límite de franquicias excedido. Tiene %d franquicia(s) y el máximo permitido es %d.",
                currentCount, maxAllowed
        ), HttpStatus.FORBIDDEN);
    }
}
