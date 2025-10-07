package com.vegas.sistema_gestion_operativa.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
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
        String phoneNumber
) {
}
