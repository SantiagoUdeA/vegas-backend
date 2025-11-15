package com.vegas.sistema_gestion_operativa.catalog.products.domain.exceptions;

public class ProductCategoryNameAlreadyExistsException extends Exception {
    public ProductCategoryNameAlreadyExistsException(String message) {
        super(message);
    }
}

