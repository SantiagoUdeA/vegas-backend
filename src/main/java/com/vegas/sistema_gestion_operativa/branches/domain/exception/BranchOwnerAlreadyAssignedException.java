package com.vegas.sistema_gestion_operativa.branches.domain.exception;

/**
 * Exception thrown when a user is already assigned as an owner of a branch.
 */
public class BranchOwnerAlreadyAssignedException extends RuntimeException {

    public BranchOwnerAlreadyAssignedException(String message) {
        super(message);
    }
}
