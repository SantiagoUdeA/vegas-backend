package com.vegas.sistema_gestion_operativa.branches.domain.exception;

public class BranchNotFoundException extends RuntimeException {

    public BranchNotFoundException(String message) {
        super(message);
    }
}
