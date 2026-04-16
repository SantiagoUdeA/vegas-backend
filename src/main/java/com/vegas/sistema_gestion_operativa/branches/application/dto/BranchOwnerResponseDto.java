package com.vegas.sistema_gestion_operativa.branches.application.dto;

/**
 * DTO representing an owner assigned to a branch.
 */
public record BranchOwnerResponseDto(
        String userId,
        String fullName,
        boolean founder
) {
}
