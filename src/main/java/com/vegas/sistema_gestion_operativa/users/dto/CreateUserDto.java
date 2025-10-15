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
        @NotNull(message = "El correo electrónico es obligatorio")
        @Email(message = "El correo electrónico no es válido")
        String email,

        @NotNull(message = "El nombre es obligatorio")
        @Length(max = 100, message = "El nombre no puede superar los 100 caracteres")
        String givenName,

        @NotNull(message = "El apellido es obligatorio")
        @Length(max = 100, message = "El apellido no puede superar los 100 caracteres")
        String familyName,

        @NotNull(message = "El tipo de documento es obligatorio")
        @Pattern(regexp = "^(CC|CE|PP)$", message = "El tipo de documento debe ser CC, CE o PP")
        String idType,

        @NotNull(message = "El número de documento es obligatorio")
        @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "El número de documento debe ser alfanumérico")
        String idNumber,

        @Nullable
        @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "El número de teléfono es inválido")
        String phoneNumber,

        @Pattern(regexp = "(?i)ADMIN|CASHIER|OWNER", message = "El rol debe ser ADMIN, CASHIER u OWNER")
        String roleName
) {
}
