package com.vegas.sistema_gestion_operativa.sales.application.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class CreateSaleDto {
    @NotNull
    Long branchId;
    @NotEmpty
    List<DetailCreateDto> details;

    @Value
    @Builder
    public static class DetailCreateDto {
        @NotNull
        Long productId;
        @NotNull
        Integer quantity;
    }
}
