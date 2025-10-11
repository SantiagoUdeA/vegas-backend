package com.vegas.sistema_gestion_operativa.users.exceptions;

import com.vegas.sistema_gestion_operativa.shared.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ApiException {
    public UserNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
