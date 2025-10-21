package com.vegas.sistema_gestion_operativa.provider.domain.exceptions;

import com.vegas.sistema_gestion_operativa.common.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class ProviderNotFoundException extends ApiException {
    public ProviderNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
