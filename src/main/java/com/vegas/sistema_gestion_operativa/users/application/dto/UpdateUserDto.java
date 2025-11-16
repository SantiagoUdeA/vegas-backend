package com.vegas.sistema_gestion_operativa.users.application.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

/**
 * Data Transfer Object for updating user information.
 * Includes validation constraints for each field.
 */
public record UpdateUserDto(
        @NotNull
        @Length(max = 100)
        String givenName,
        @NotNull
        @Length(max = 100)
        String familyName,
        String idType,
        String idNumber,

        @Nullable
        @Pattern(regexp = "^\\+?\\d{7,15}$", message = "Número de teléfono inválido")
        String phoneNumber,

        String roleName
) {
}
