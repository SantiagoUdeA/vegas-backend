package com.vegas.sistema_gestion_operativa.users.application.dto;

import com.vegas.sistema_gestion_operativa.users.domain.entity.IdType;
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
        IdType idType,

        @NotNull(message = "El número de documento es obligatorio")
        @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "El número de documento debe ser alfanumérico")
        String idNumber,

        @Nullable
        @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "El número de teléfono es inválido")
        String phoneNumber,

        @NotNull(message = "El rol es obligatorio")
        String roleName,

        @Nullable
        Long branchId
) {
}
