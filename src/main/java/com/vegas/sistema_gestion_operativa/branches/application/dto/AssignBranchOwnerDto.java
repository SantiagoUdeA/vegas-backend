package com.vegas.sistema_gestion_operativa.branches.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO for assigning a new owner (co-owner) to a branch.
 * The owner is identified by their registered email address.
 */
public record AssignBranchOwnerDto(
        @NotBlank(message = "El correo electrónico es obligatorio")
        @Email(message = "El correo electrónico no tiene un formato válido")
        String email
) {
}

