package com.vegas.sistema_gestion_operativa.products.domain.exceptions;

import com.vegas.sistema_gestion_operativa.common.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class RecipeNotFoundException extends ApiException {

    public RecipeNotFoundException(Long recipeId) {
        super("Receta no encontrada con ID: " + recipeId, HttpStatus.NOT_FOUND);
    }
}
