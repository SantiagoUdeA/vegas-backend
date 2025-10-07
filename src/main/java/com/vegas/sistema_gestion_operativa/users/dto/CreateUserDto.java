package com.vegas.sistema_gestion_operativa.users.dto;

public record CreateUserDto(
        String email,
        String givenName,
        String familyName,
        String idType,
        String phoneNumber
) {
}
