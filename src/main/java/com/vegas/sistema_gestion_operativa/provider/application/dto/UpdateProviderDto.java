package com.vegas.sistema_gestion_operativa.provider.application.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record UpdateProviderDto(
        @NotNull(message = "El nombre del proveedor es obligatorio")
        @Length(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
        String name,

        @NotNull(message = "El NIT es obligatorio")
        @Length(min = 5, max = 100, message = "El NIT debe tener entre 5 y 100 caracteres")
        String nit,

        @Length(max = 20, message = "El número de teléfono no puede exceder 20 caracteres")
        String phoneNumber
){}
