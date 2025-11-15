package com.vegas.sistema_gestion_operativa.catalog.provider.application.service;

import com.vegas.sistema_gestion_operativa.catalog.provider.application.dto.CreateProviderDto;
import com.vegas.sistema_gestion_operativa.catalog.provider.application.dto.UpdateProviderDto;
import com.vegas.sistema_gestion_operativa.catalog.provider.application.factory.ProviderFactory;
import com.vegas.sistema_gestion_operativa.catalog.provider.application.mapper.IProviderMapper;
import com.vegas.sistema_gestion_operativa.catalog.provider.domain.entity.Provider;
import com.vegas.sistema_gestion_operativa.catalog.provider.domain.exceptions.ProviderNotFoundException;
import com.vegas.sistema_gestion_operativa.catalog.provider.infrastructure.repository.IProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProviderService {

    private final ProviderFactory providerFactory;
    private final IProviderRepository providerRepository;
    private final IProviderMapper providerMapper;

    @Autowired
    public ProviderService(ProviderFactory providerFactory, IProviderRepository providerRepository, IProviderMapper providerMapper) {
        this.providerFactory = providerFactory;
        this.providerRepository = providerRepository;
        this.providerMapper = providerMapper;
    }

    public Page<Provider> findAll(Pageable pageable){
        return providerRepository.findAllByActiveTrue(pageable);
    }

    public Provider create(CreateProviderDto dto){
        return this.providerRepository.save(this.providerFactory.createFromDto(dto));
    }

    public Provider update(Long providerId, UpdateProviderDto dto) throws ProviderNotFoundException {
        var provider = this.retrieveProviderById(providerId);
        var updatedProvider = this.providerMapper.partialUpdate(dto, provider);
        return this.providerRepository.save(updatedProvider);
    }

    public Provider delete(Long providerId) throws ProviderNotFoundException {
        var provider = this.retrieveProviderById(providerId);
        provider.deactivate();
        this.providerRepository.save(provider);
        return provider;
    }

    private Provider retrieveProviderById(Long id) throws ProviderNotFoundException {
        return this.providerRepository.findById(id)
                .orElseThrow(() -> new ProviderNotFoundException("El proveedor con id " + id + " no fue encontrado"));
    }
}
