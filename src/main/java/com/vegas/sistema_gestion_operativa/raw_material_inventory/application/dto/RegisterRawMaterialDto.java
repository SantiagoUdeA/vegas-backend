package com.vegas.sistema_gestion_operativa.raw_material_inventory.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RegisterRawMaterialDto(
        @NotNull(message = "El ID del material prima no puede ser nulo")
        Long rawMaterialId,

        @NotNull(message = "El ID de la sede no puede ser nulo")
        Long branchId,

        @NotNull(message = "La cantidad no puede ser nula")
        @Positive(message = "La cantidad debe ser mayor a cero")
        Double quantity
) {
}
