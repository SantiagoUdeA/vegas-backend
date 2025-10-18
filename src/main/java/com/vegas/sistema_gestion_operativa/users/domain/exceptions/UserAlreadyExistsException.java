package com.vegas.sistema_gestion_operativa.users.domain.exceptions;

import com.vegas.sistema_gestion_operativa.common.exceptions.BadRequestException;

public class UserAlreadyExistsException extends BadRequestException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }

}
