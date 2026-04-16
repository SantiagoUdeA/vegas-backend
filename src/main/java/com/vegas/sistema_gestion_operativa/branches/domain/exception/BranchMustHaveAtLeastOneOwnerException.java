package com.vegas.sistema_gestion_operativa.branches.domain.exception;

/**
 * Exception thrown when an operation would leave a branch without any owner.
 */
public class BranchMustHaveAtLeastOneOwnerException extends RuntimeException {

    public BranchMustHaveAtLeastOneOwnerException(String message) {
        super(message);
    }
}
