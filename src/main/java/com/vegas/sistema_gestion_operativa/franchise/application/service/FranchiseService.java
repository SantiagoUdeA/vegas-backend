package com.vegas.sistema_gestion_operativa.franchise.application.service;

import com.vegas.sistema_gestion_operativa.franchise.IFranchiseApi;
import com.vegas.sistema_gestion_operativa.franchise.application.dto.CreateFranchiseDto;
import com.vegas.sistema_gestion_operativa.franchise.application.dto.FranchiseResponseDto;
import com.vegas.sistema_gestion_operativa.franchise.application.dto.UpdateFranchiseDto;
import com.vegas.sistema_gestion_operativa.franchise.application.factory.FranchiseFactory;
import com.vegas.sistema_gestion_operativa.franchise.application.mapper.IFranchiseMapper;
import com.vegas.sistema_gestion_operativa.franchise.domain.entity.Franchise;
import com.vegas.sistema_gestion_operativa.franchise.domain.exception.FranchiseNameAlreadyExistsException;
import com.vegas.sistema_gestion_operativa.franchise.domain.exception.FranchiseNotFoundException;
import com.vegas.sistema_gestion_operativa.franchise.infrastructure.repository.IFranchiseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FranchiseService implements IFranchiseApi {

    private final IFranchiseRepository franchiseRepository;
    private final IFranchiseMapper franchiseMapper;

    @Autowired
    public FranchiseService(IFranchiseRepository franchiseRepository,
                            IFranchiseMapper franchiseMapper) {
        this.franchiseRepository = franchiseRepository;
        this.franchiseMapper = franchiseMapper;
    }

    public Page<FranchiseResponseDto> findAll(Pageable pageable) {
        return franchiseRepository.findAll(pageable).map(franchiseMapper::toResponseDto);
    }

    public FranchiseResponseDto findById(Long id) throws FranchiseNotFoundException {
        var franchise = retrieveFranchiseById(id);
        return franchiseMapper.toResponseDto(franchise);
    }

    public FranchiseResponseDto create(CreateFranchiseDto dto) throws FranchiseNameAlreadyExistsException {
        var existing = franchiseRepository.findByName(dto.name());
        if (existing.isPresent())
            throw new FranchiseNameAlreadyExistsException(dto.name());
        if (Optional.ofNullable(dto.slug()).isEmpty() || dto.slug().isBlank()) {
            dto = new CreateFranchiseDto(dto.name(), dto.name().toLowerCase().replaceAll("\\s+", "-"));
        }
        Franchise franchise = franchiseRepository.save(FranchiseFactory.createFranchise(dto));
        return franchiseMapper.toResponseDto(franchise);
    }

    public FranchiseResponseDto update(Long id, UpdateFranchiseDto dto) throws FranchiseNotFoundException {
        var franchise = retrieveFranchiseById(id);
        var updated = franchiseMapper.partialUpdate(dto, franchise);
        return franchiseMapper.toResponseDto(franchiseRepository.save(updated));
    }

    public FranchiseResponseDto delete(Long id) throws FranchiseNotFoundException {
        var franchise = retrieveFranchiseById(id);
        franchise.deactivate();
        franchiseRepository.save(franchise);
        return franchiseMapper.toResponseDto(franchise);
    }

    @Override
    public void assertFranchiseExists(Long franchiseId) throws FranchiseNotFoundException {
        if (!franchiseRepository.existsById(franchiseId))
            throw new FranchiseNotFoundException(franchiseId);
    }

    private Franchise retrieveFranchiseById(Long id) throws FranchiseNotFoundException {
        return franchiseRepository.findById(id)
                .orElseThrow(() -> new FranchiseNotFoundException(id));
    }
}
