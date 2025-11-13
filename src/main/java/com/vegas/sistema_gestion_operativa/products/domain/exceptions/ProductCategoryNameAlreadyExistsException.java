package com.vegas.sistema_gestion_operativa.products.domain.exceptions;

public class ProductCategoryNameAlreadyExistsException extends Exception {
    public ProductCategoryNameAlreadyExistsException(String message) {
        super(message);
    }
}

