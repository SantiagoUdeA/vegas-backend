package com.vegas.sistema_gestion_operativa.subscription.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionResponseDto {
    private Long id;
    private String ownerUserId;
    private String ownerFullName;
    private int maxFranchises;
    private int currentFranchiseCount;
    private LocalDateTime createdAt;
}
