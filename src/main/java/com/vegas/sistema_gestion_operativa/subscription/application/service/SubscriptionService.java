package com.vegas.sistema_gestion_operativa.subscription.application.service;

import com.vegas.sistema_gestion_operativa.franchise.infrastructure.repository.IOwnerFranchiseRepository;
import com.vegas.sistema_gestion_operativa.roles.IRoleApi;
import com.vegas.sistema_gestion_operativa.security.AuthUtils;
import com.vegas.sistema_gestion_operativa.subscription.ISubscriptionApi;
import com.vegas.sistema_gestion_operativa.subscription.application.dto.SubscriptionResponseDto;
import com.vegas.sistema_gestion_operativa.subscription.application.dto.UpdateSubscriptionDto;
import com.vegas.sistema_gestion_operativa.subscription.application.factory.SubscriptionFactory;
import com.vegas.sistema_gestion_operativa.subscription.application.mapper.ISubscriptionMapper;
import com.vegas.sistema_gestion_operativa.subscription.domain.entity.Subscription;
import com.vegas.sistema_gestion_operativa.subscription.domain.exception.FranchiseLimitExceededException;
import com.vegas.sistema_gestion_operativa.subscription.domain.exception.NoActiveSubscriptionException;
import com.vegas.sistema_gestion_operativa.subscription.domain.exception.SubscriptionNotFoundException;
import com.vegas.sistema_gestion_operativa.subscription.infrastructure.repository.ISubscriptionRepository;
import com.vegas.sistema_gestion_operativa.users.IUserApi;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubscriptionService implements ISubscriptionApi {

    private final ISubscriptionRepository subscriptionRepository;
    private final IOwnerFranchiseRepository ownerFranchiseRepository;
    private final ISubscriptionMapper subscriptionMapper;
    private final IUserApi userApi;
    private final IRoleApi roleApi;

    @Value("${app.subscription.default-max-franchises:3}")
    private int defaultMaxFranchises;

    /**
     * Lista todas las subscriptions (solo ROOT).
     */
    public Page<SubscriptionResponseDto> findAll(Pageable pageable) {
        return subscriptionRepository.findAll(pageable)
                .map(this::enrichResponseDto);
    }

    /**
     * Obtiene una subscription por ownerUserId.
     * ROOT puede ver cualquiera, OWNER solo la suya.
     */
    public SubscriptionResponseDto findByOwnerUserId(String ownerUserId)
            throws SubscriptionNotFoundException {
        String currentUserId = AuthUtils.getUserIdFromToken();
        String currentRole = AuthUtils.getRoleNameFromToken();

        // OWNER solo puede ver su propia subscription
        if (roleApi.isOwnerRole(currentRole) && !currentUserId.equals(ownerUserId)) {
            throw new AccessDeniedException("Solo puedes consultar tu propia suscripción");
        }

        Subscription subscription = subscriptionRepository.findByOwnerUserId(ownerUserId)
                .orElseThrow(() -> new SubscriptionNotFoundException(ownerUserId));

        return enrichResponseDto(subscription);
    }

    /**
     * Actualiza el maxFranchises de una subscription (solo ROOT).
     */
    @Transactional
    public SubscriptionResponseDto update(String ownerUserId, UpdateSubscriptionDto dto)
            throws SubscriptionNotFoundException {
        Subscription subscription = subscriptionRepository.findByOwnerUserId(ownerUserId)
                .orElseThrow(() -> new SubscriptionNotFoundException(ownerUserId));

        Subscription updated = subscriptionMapper.partialUpdate(dto, subscription);
        Subscription saved = subscriptionRepository.save(updated);

        return enrichResponseDto(saved);
    }

    /**
     * Crea una subscription para un nuevo OWNER (llamado internamente via evento).
     */
    @Transactional
    public Subscription createForOwner(String ownerUserId) {
        if (subscriptionRepository.existsByOwnerUserId(ownerUserId)) {
            return subscriptionRepository.findByOwnerUserId(ownerUserId).orElseThrow();
        }

        Subscription subscription = SubscriptionFactory.createSubscription(
                ownerUserId,
                defaultMaxFranchises
        );
        return subscriptionRepository.save(subscription);
    }

    // === Implementacion de ISubscriptionApi ===

    @Override
    public void validateFranchiseLimit(String ownerUserId, int currentFranchiseCount)
            throws NoActiveSubscriptionException, FranchiseLimitExceededException {
        int maxFranchises = getMaxFranchises(ownerUserId);

        if (currentFranchiseCount >= maxFranchises) {
            throw new FranchiseLimitExceededException(currentFranchiseCount, maxFranchises);
        }
    }

    @Override
    public int getMaxFranchises(String ownerUserId) throws NoActiveSubscriptionException {
        return subscriptionRepository.findByOwnerUserId(ownerUserId)
                .map(Subscription::getMaxFranchises)
                .orElseThrow(() -> new NoActiveSubscriptionException(ownerUserId));
    }

    // === Metodos privados ===

    private SubscriptionResponseDto enrichResponseDto(Subscription subscription) {
        SubscriptionResponseDto dto = subscriptionMapper.toResponseDto(subscription);

        dto.setOwnerFullName(userApi.getFullNameById(subscription.getOwnerUserId()));

        int franchiseCount = ownerFranchiseRepository
                .findFranchiseIdsByOwnerId(subscription.getOwnerUserId())
                .size();
        dto.setCurrentFranchiseCount(franchiseCount);

        return dto;
    }
}
