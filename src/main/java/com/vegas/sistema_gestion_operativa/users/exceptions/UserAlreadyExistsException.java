package com.vegas.sistema_gestion_operativa.users.exceptions;

import com.vegas.sistema_gestion_operativa.shared.exceptions.BadRequestException;

public class UserAlreadyExistsException extends BadRequestException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }

}
