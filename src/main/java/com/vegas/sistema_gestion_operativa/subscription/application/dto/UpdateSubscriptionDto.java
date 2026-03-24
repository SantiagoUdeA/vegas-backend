package com.vegas.sistema_gestion_operativa.subscription.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateSubscriptionDto(
        @NotNull(message = "El límite de franquicias es obligatorio")
        @Min(value = 1, message = "El límite de franquicias debe ser al menos 1")
        Integer maxFranchises
) {
}
