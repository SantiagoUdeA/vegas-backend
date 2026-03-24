package com.vegas.sistema_gestion_operativa.branches.application.dto;

public record BranchResponseDto(
        Long id,
        String name,
        String address,
        String phoneNumber,
        Long franchiseId,
        String franchiseName
) {
}
