package com.vegas.sistema_gestion_operativa.products.api;

import com.vegas.sistema_gestion_operativa.products.domain.exceptions.ProductNotFoundException;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;

public interface IProductApi {

    Optional<List<IngredientDto>> getRawMaterialIngredients(@NotNull(message = "El ID del producto es obligatorio") Long productId) throws ProductNotFoundException;
}
