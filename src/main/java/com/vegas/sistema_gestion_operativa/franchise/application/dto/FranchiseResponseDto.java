package com.vegas.sistema_gestion_operativa.franchise.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FranchiseResponseDto {
    private Long id;
    private String name;
    private String slug;
    private boolean active;
    private LocalDateTime createdAt;
}
