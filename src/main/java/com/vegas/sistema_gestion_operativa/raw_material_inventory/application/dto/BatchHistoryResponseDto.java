package com.vegas.sistema_gestion_operativa.raw_material_inventory.application.dto;

import com.vegas.sistema_gestion_operativa.common.dto.PageResponse;

public record BatchHistoryResponseDto(
        BatchDetailDto batch,
        PageResponse<BatchMovementItemDto> movements
) {}
