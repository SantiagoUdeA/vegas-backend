package com.vegas.sistema_gestion_operativa.users.dto;

/**
 * Data Transfer Object for creating a new user.
 * Contains necessary fields for user creation.
 */
public record CreateUserDto(
        String email,
        String givenName,
        String familyName,
        String idType,
        String phoneNumber
) {
}
