package com.vegas.sistema_gestion_operativa.products_inventory.domain.exceptions;

public class InsufficientStockException extends Exception {
    public InsufficientStockException(String message) {
        super(message);
    }
}

