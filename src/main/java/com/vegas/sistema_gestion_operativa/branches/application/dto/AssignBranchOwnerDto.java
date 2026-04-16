package com.vegas.sistema_gestion_operativa.branches.application.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for assigning a new owner (co-owner) to a branch.
 */
public record AssignBranchOwnerDto(
        @NotBlank(message = "El ID del usuario objetivo es obligatorio")
        String targetUserId
) {
}
