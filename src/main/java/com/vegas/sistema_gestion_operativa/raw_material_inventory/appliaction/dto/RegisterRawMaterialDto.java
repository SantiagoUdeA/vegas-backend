package com.vegas.sistema_gestion_operativa.raw_material_inventory.appliaction.dto;

import jakarta.validation.constraints.NotNull;

public record RegisterRawMaterialDto(
        @NotNull(message = "El ID del material prima no puede ser nulo")
        Long rawMaterialId,

        @NotNull(message = "El ID de la sede no puede ser nulo")
        Long branchId,

        @NotNull(message = "La cantidad no puede ser nula")
        Double quantity
) {
}
