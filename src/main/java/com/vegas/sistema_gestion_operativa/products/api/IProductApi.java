package com.vegas.sistema_gestion_operativa.products.api;

import com.vegas.sistema_gestion_operativa.products.domain.exceptions.ProductNotFoundException;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;

public interface IProductApi {

    Optional<List<IngredientDto>> getIngredientsForProductUnit(
            @NotNull(message = "El ID del producto es obligatorio") Long productId
    ) throws ProductNotFoundException;

    // 🔥 Nuevo: obtener nombre del producto
    String getProductNameById(
            @NotNull(message = "El ID del producto es obligatorio") Long productId
    ) throws ProductNotFoundException;
}