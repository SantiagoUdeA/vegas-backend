package com.vegas.sistema_gestion_operativa.provider.application.service;

import com.vegas.sistema_gestion_operativa.branches.IBranchApi;
import com.vegas.sistema_gestion_operativa.common.exceptions.AccessDeniedException;
import com.vegas.sistema_gestion_operativa.provider.application.dto.CreateProviderDto;
import com.vegas.sistema_gestion_operativa.provider.application.dto.UpdateProviderDto;
import com.vegas.sistema_gestion_operativa.provider.application.factory.ProviderFactory;
import com.vegas.sistema_gestion_operativa.provider.application.mapper.IProviderMapper;
import com.vegas.sistema_gestion_operativa.provider.domain.entity.Provider;
import com.vegas.sistema_gestion_operativa.provider.domain.exceptions.ProviderNotFoundException;
import com.vegas.sistema_gestion_operativa.provider.infrastructure.repository.IProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProviderService {

    private final ProviderFactory providerFactory;
    private final IProviderRepository providerRepository;
    private final IProviderMapper providerMapper;
    private final IBranchApi branchApi;

    @Autowired
    public ProviderService(ProviderFactory providerFactory,
                           IProviderRepository providerRepository,
                           IProviderMapper providerMapper,
                           IBranchApi branchApi) {
        this.providerFactory = providerFactory;
        this.providerRepository = providerRepository;
        this.providerMapper = providerMapper;
        this.branchApi = branchApi;
    }

    @Transactional(readOnly = true)
    public Page<Provider> findAll(Pageable pageable, String userId, Long branchId)
            throws AccessDeniedException {
        branchApi.assertUserHasAccessToBranch(userId, branchId);
        return providerRepository.findAllByActiveTrueAndBranchId(branchId, pageable);
    }

    public Provider create(CreateProviderDto dto, String userId) throws AccessDeniedException {
        branchApi.assertUserHasAccessToBranch(userId, dto.branchId());
        return this.providerRepository.save(this.providerFactory.createFromDto(dto, dto.branchId()));
    }

    public Provider update(Long providerId, UpdateProviderDto dto, String userId)
            throws ProviderNotFoundException, AccessDeniedException {
        var provider = this.retrieveProviderById(providerId);
        branchApi.assertUserHasAccessToBranch(userId, provider.getBranchId());

        var updatedProvider = this.providerMapper.partialUpdate(dto, provider);
        return this.providerRepository.save(updatedProvider);
    }

    public Provider delete(Long providerId, String userId)
            throws ProviderNotFoundException, AccessDeniedException {
        var provider = this.retrieveProviderById(providerId);
        branchApi.assertUserHasAccessToBranch(userId, provider.getBranchId());

        provider.deactivate();
        this.providerRepository.save(provider);
        return provider;
    }

    private Provider retrieveProviderById(Long id) throws ProviderNotFoundException {
        return this.providerRepository.findById(id)
                .orElseThrow(() -> new ProviderNotFoundException("El proveedor con id " + id + " no fue encontrado"));
    }
}
