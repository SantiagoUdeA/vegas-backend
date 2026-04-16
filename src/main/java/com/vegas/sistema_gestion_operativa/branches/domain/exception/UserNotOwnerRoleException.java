package com.vegas.sistema_gestion_operativa.branches.domain.exception;

/**
 * Exception thrown when trying to assign a user as branch owner
 * but the user does not have the OWNER role.
 */
public class UserNotOwnerRoleException extends RuntimeException {

    public UserNotOwnerRoleException(String message) {
        super(message);
    }
}
