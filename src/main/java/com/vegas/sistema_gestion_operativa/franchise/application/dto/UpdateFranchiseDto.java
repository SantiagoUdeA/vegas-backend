package com.vegas.sistema_gestion_operativa.franchise.application.dto;

import org.hibernate.validator.constraints.Length;

public record UpdateFranchiseDto(
        @Length(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
        String name,

        @Length(min = 3, max = 100, message = "El slug debe tener entre 3 y 100 caracteres")
        String slug
) {
}
