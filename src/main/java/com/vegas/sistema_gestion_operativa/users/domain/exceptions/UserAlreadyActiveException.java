package com.vegas.sistema_gestion_operativa.users.domain.exceptions;

import com.vegas.sistema_gestion_operativa.common.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class UserAlreadyActiveException extends ApiException {

    public UserAlreadyActiveException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
