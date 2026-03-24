package com.vegas.sistema_gestion_operativa.franchise.application.service;

import com.vegas.sistema_gestion_operativa.franchise.IFranchiseApi;
import com.vegas.sistema_gestion_operativa.franchise.application.dto.CreateFranchiseDto;
import com.vegas.sistema_gestion_operativa.franchise.application.dto.FranchiseResponseDto;
import com.vegas.sistema_gestion_operativa.franchise.application.dto.UpdateFranchiseDto;
import com.vegas.sistema_gestion_operativa.franchise.application.factory.FranchiseFactory;
import com.vegas.sistema_gestion_operativa.franchise.application.mapper.IFranchiseMapper;
import com.vegas.sistema_gestion_operativa.franchise.domain.entity.Franchise;
import com.vegas.sistema_gestion_operativa.franchise.domain.entity.OwnerFranchise;
import com.vegas.sistema_gestion_operativa.franchise.domain.exception.FranchiseAccessDeniedException;
import com.vegas.sistema_gestion_operativa.franchise.domain.exception.FranchiseNameAlreadyExistsException;
import com.vegas.sistema_gestion_operativa.franchise.domain.exception.FranchiseNotFoundException;
import com.vegas.sistema_gestion_operativa.franchise.infrastructure.repository.IFranchiseRepository;
import com.vegas.sistema_gestion_operativa.franchise.infrastructure.repository.IOwnerFranchiseRepository;
import com.vegas.sistema_gestion_operativa.security.AuthUtils;
import com.vegas.sistema_gestion_operativa.subscription.ISubscriptionApi;
import com.vegas.sistema_gestion_operativa.subscription.domain.exception.FranchiseLimitExceededException;
import com.vegas.sistema_gestion_operativa.subscription.domain.exception.NoActiveSubscriptionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
public class FranchiseService implements IFranchiseApi {

    private final IFranchiseRepository franchiseRepository;
    private final IOwnerFranchiseRepository ownerFranchiseRepository;
    private final IFranchiseMapper franchiseMapper;
    private final ISubscriptionApi subscriptionApi;

    @Autowired
    public FranchiseService(IFranchiseRepository franchiseRepository,
                            IOwnerFranchiseRepository ownerFranchiseRepository,
                            IFranchiseMapper franchiseMapper,
                            ISubscriptionApi subscriptionApi) {
        this.franchiseRepository = franchiseRepository;
        this.ownerFranchiseRepository = ownerFranchiseRepository;
        this.franchiseMapper = franchiseMapper;
        this.subscriptionApi = subscriptionApi;
    }

    public Page<FranchiseResponseDto> findAll(Pageable pageable) {
        String ownerId = AuthUtils.getUserIdFromToken();
        Set<Long> ownerFranchiseIds = ownerFranchiseRepository.findFranchiseIdsByOwnerId(ownerId);

        if (ownerFranchiseIds.isEmpty()) {
            return Page.empty(pageable);
        }

        Page<Franchise> franchisesPage = franchiseRepository.findByIdIn(ownerFranchiseIds, pageable);
        return franchisesPage.map(franchiseMapper::toResponseDto);
    }

    public FranchiseResponseDto findById(Long id) throws FranchiseNotFoundException, FranchiseAccessDeniedException {
        assertOwnerBelongsToFranchise(id);
        var franchise = retrieveFranchiseById(id);
        return franchiseMapper.toResponseDto(franchise);
    }

    @Transactional
    public FranchiseResponseDto create(CreateFranchiseDto dto)
            throws FranchiseNameAlreadyExistsException, NoActiveSubscriptionException, FranchiseLimitExceededException {
        // Obtener el OWNER actual
        String ownerId = AuthUtils.getUserIdFromToken();

        // Validar límite de franquicias ANTES de crear
        int currentCount = ownerFranchiseRepository.findFranchiseIdsByOwnerId(ownerId).size();
        subscriptionApi.validateFranchiseLimit(ownerId, currentCount);

        var existing = franchiseRepository.findByName(dto.name());
        if (existing.isPresent())
            throw new FranchiseNameAlreadyExistsException(dto.name());
        if (Optional.ofNullable(dto.slug()).isEmpty() || dto.slug().isBlank()) {
            dto = new CreateFranchiseDto(dto.name(), dto.name().toLowerCase().replaceAll("\\s+", "-"));
        }
        Franchise franchise = franchiseRepository.save(FranchiseFactory.createFranchise(dto));

        // Asignar automáticamente el OWNER a la franquicia creada
        var ownerFranchise = OwnerFranchise.builder()
                .ownerId(ownerId)
                .franchiseId(franchise.getId())
                .build();
        ownerFranchiseRepository.save(ownerFranchise);

        return franchiseMapper.toResponseDto(franchise);
    }

    public FranchiseResponseDto update(Long id, UpdateFranchiseDto dto) throws FranchiseNotFoundException, FranchiseAccessDeniedException {
        assertOwnerBelongsToFranchise(id);
        var franchise = retrieveFranchiseById(id);
        var updated = franchiseMapper.partialUpdate(dto, franchise);
        return franchiseMapper.toResponseDto(franchiseRepository.save(updated));
    }

    public FranchiseResponseDto delete(Long id) throws FranchiseNotFoundException, FranchiseAccessDeniedException {
        assertOwnerBelongsToFranchise(id);
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

    @Override
    public void assertUserOwnsFranchise(String userId, Long franchiseId) throws FranchiseAccessDeniedException {
        if (!ownerFranchiseRepository.existsByOwnerIdAndFranchiseId(userId, franchiseId)) {
            throw new FranchiseAccessDeniedException(franchiseId);
        }
    }

    @Override
    public String getFranchiseName(Long franchiseId) {
        return franchiseRepository.findById(franchiseId)
                .map(Franchise::getName)
                .orElse(null);
    }

    private Franchise retrieveFranchiseById(Long id) throws FranchiseNotFoundException {
        return franchiseRepository.findById(id)
                .orElseThrow(() -> new FranchiseNotFoundException(id));
    }

    private void assertOwnerBelongsToFranchise(Long franchiseId) throws FranchiseAccessDeniedException {
        String ownerId = AuthUtils.getUserIdFromToken();
        assertUserOwnsFranchise(ownerId, franchiseId);
    }
}
