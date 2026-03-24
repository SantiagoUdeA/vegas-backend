package com.vegas.sistema_gestion_operativa.subscription.infrastructure.controller;

import com.vegas.sistema_gestion_operativa.common.dto.PageResponse;
import com.vegas.sistema_gestion_operativa.common.dto.PaginationRequest;
import com.vegas.sistema_gestion_operativa.common.utils.PaginationUtils;
import com.vegas.sistema_gestion_operativa.subscription.application.dto.SubscriptionResponseDto;
import com.vegas.sistema_gestion_operativa.subscription.application.dto.UpdateSubscriptionDto;
import com.vegas.sistema_gestion_operativa.subscription.application.service.SubscriptionService;
import com.vegas.sistema_gestion_operativa.subscription.domain.exception.SubscriptionNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    /**
     * GET /api/v1/subscriptions
     * Lista todas las subscriptions (solo ROOT).
     */
    @GetMapping
    @PreAuthorize("hasPermission(null, 'SUBSCRIPTIONS_VIEW') and hasRole('ROOT')")
    public ResponseEntity<PageResponse<SubscriptionResponseDto>> findAll(
            PaginationRequest paginationRequest) {
        Pageable pageable = PaginationUtils.getPageable(paginationRequest);
        Page<SubscriptionResponseDto> page = subscriptionService.findAll(pageable);
        return ResponseEntity.ok(PageResponse.from(page));
    }

    /**
     * GET /api/v1/subscriptions/{ownerUserId}
     * Obtiene una subscription especifica (ROOT cualquiera, OWNER solo la suya).
     */
    @GetMapping("/{ownerUserId}")
    @PreAuthorize("hasPermission(null, 'SUBSCRIPTIONS_VIEW')")
    public ResponseEntity<SubscriptionResponseDto> findByOwnerUserId(
            @PathVariable String ownerUserId) throws SubscriptionNotFoundException {
        return ResponseEntity.ok(subscriptionService.findByOwnerUserId(ownerUserId));
    }

    /**
     * PUT /api/v1/subscriptions/{ownerUserId}
     * Actualiza el maxFranchises (solo ROOT).
     */
    @PutMapping("/{ownerUserId}")
    @PreAuthorize("hasPermission(null, 'SUBSCRIPTIONS_EDIT')")
    public ResponseEntity<SubscriptionResponseDto> update(
            @PathVariable String ownerUserId,
            @RequestBody @Valid UpdateSubscriptionDto dto) throws SubscriptionNotFoundException {
        return ResponseEntity.ok(subscriptionService.update(ownerUserId, dto));
    }
}
