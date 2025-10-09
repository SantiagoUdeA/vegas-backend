package com.vegas.sistema_gestion_operativa.users.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

/**
 * Data Transfer Object for creating a new user.
 * Contains necessary fields for user creation.
 */
public record CreateUserDto(
        @NotNull
        @Email
        String email,
        @NotNull
        @Length(max = 100)
        String givenName,
        @NotNull
        @Length(max = 100)
        String familyName,
        String idType,
        String idNumber,

        @Nullable
        @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Número de teléfono inválido")
        String phoneNumber,

        @Pattern(regexp = "(?i)ADMIN|CASHIER|OWNER", message = "Rol inválido")
        String roleName
) {
}
