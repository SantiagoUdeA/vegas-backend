package com.vegas.sistema_gestion_operativa.franchise.application.listener;

import com.vegas.sistema_gestion_operativa.franchise.domain.entity.OwnerFranchise;
import com.vegas.sistema_gestion_operativa.franchise.infrastructure.repository.IOwnerFranchiseRepository;
import com.vegas.sistema_gestion_operativa.roles.IRoleApi;
import com.vegas.sistema_gestion_operativa.users.domain.events.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class OwnerFranchiseCreatedListener {

    private final IOwnerFranchiseRepository ownerFranchiseRepository;
    private final IRoleApi roleApi;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleUserCreated(UserCreatedEvent event) {
        if (!roleApi.isOwnerRole(event.roleName()) || event.franchiseId().isEmpty()) {
            return;
        }

        Long franchiseId = event.franchiseId().get();
        if (!ownerFranchiseRepository.existsByOwnerIdAndFranchiseId(event.userId(), franchiseId)) {
            ownerFranchiseRepository.save(OwnerFranchise.builder()
                    .ownerId(event.userId())
                    .franchiseId(franchiseId)
                    .build());
        }
    }
}
