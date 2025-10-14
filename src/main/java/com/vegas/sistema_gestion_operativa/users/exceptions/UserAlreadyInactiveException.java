package com.vegas.sistema_gestion_operativa.users.exceptions;

import com.vegas.sistema_gestion_operativa.shared.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class UserAlreadyInactiveException extends ApiException {
    public UserAlreadyInactiveException(String message) {
        super(message, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
