package com.vegas.sistema_gestion_operativa.provider.infrastructure.controller;

import com.vegas.sistema_gestion_operativa.common.dto.PageResponse;
import com.vegas.sistema_gestion_operativa.common.dto.PaginationRequest;
import com.vegas.sistema_gestion_operativa.common.exceptions.AccessDeniedException;
import com.vegas.sistema_gestion_operativa.common.utils.PaginationUtils;
import com.vegas.sistema_gestion_operativa.provider.application.dto.CreateProviderDto;
import com.vegas.sistema_gestion_operativa.provider.application.dto.UpdateProviderDto;
import com.vegas.sistema_gestion_operativa.provider.application.service.ProviderService;
import com.vegas.sistema_gestion_operativa.provider.domain.entity.Provider;
import com.vegas.sistema_gestion_operativa.provider.domain.exceptions.ProviderNotFoundException;
import com.vegas.sistema_gestion_operativa.security.AuthUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for provider management.
 * Provides endpoints to manage providers.
 */
@RestController
@RequestMapping("/api/v1/providers")
public class ProviderController {

    private final ProviderService providerService;

    @Autowired
    public ProviderController(ProviderService providerService) {
        this.providerService = providerService;
    }

    /**
     * Retrieves the list of all providers for a branch.
     *
     * @param paginationRequest pagination parameters
     * @param branchId          branch identifier
     * @return paginated list of providers
     */
    @GetMapping
    @PreAuthorize("hasPermission(null, 'PROVIDERS_VIEW')")
    public ResponseEntity<PageResponse<Provider>> findAll(
            PaginationRequest paginationRequest,
            @RequestParam Long branchId
    ) throws AccessDeniedException {
        Pageable pageable = PaginationUtils.getPageable(paginationRequest);
        Page<Provider> providerPage = providerService.findAll(
                pageable,
                AuthUtils.getUserIdFromToken(),
                branchId
        );
        return ResponseEntity.ok(PageResponse.from(providerPage));
    }

    /**
     * Creates a new provider.
     *
     * @param dto data to create the provider
     * @return created provider
     */
    @PostMapping
    @PreAuthorize("hasPermission(null, 'PROVIDERS_CREATE')")
    public ResponseEntity<Provider> create(@RequestBody @Valid CreateProviderDto dto)
            throws AccessDeniedException {
        Provider provider = providerService.create(dto, AuthUtils.getUserIdFromToken());
        return ResponseEntity.ok(provider);
    }

    /**
     * Updates an existing provider.
     *
     * @param providerId the ID of the provider to update
     * @param dto        data to update the provider
     * @return updated provider
     */
    @PatchMapping("/{providerId}")
    @PreAuthorize("hasPermission(null, 'PROVIDERS_EDIT')")
    public ResponseEntity<Provider> update(
            @PathVariable Long providerId,
            @RequestBody @Valid UpdateProviderDto dto
    ) throws ProviderNotFoundException, AccessDeniedException {
        Provider updated = providerService.update(providerId, dto, AuthUtils.getUserIdFromToken());
        return ResponseEntity.ok(updated);
    }

    /**
     * Deletes a provider.
     *
     * @param providerId the ID of the provider to delete
     * @return deleted provider
     */
    @DeleteMapping("/{providerId}")
    @PreAuthorize("hasPermission(null, 'PROVIDERS_DELETE')")
    public ResponseEntity<Provider> delete(@PathVariable Long providerId)
            throws ProviderNotFoundException, AccessDeniedException {
        Provider deleted = providerService.delete(providerId, AuthUtils.getUserIdFromToken());
        return ResponseEntity.ok(deleted);
    }
}
