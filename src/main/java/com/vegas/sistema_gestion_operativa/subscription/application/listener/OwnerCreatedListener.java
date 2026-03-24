package com.vegas.sistema_gestion_operativa.subscription.application.listener;

import com.vegas.sistema_gestion_operativa.roles.IRoleApi;
import com.vegas.sistema_gestion_operativa.subscription.application.service.SubscriptionService;
import com.vegas.sistema_gestion_operativa.users.domain.events.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class OwnerCreatedListener {

    private final SubscriptionService subscriptionService;
    private final IRoleApi roleApi;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleUserCreated(UserCreatedEvent event) {
        if (roleApi.isOwnerRole(event.roleName()) && roleApi.isRootRole(event.issuerRoleName())) {
            log.info("Creando suscripción para nuevo OWNER: {}", event.userId());
            subscriptionService.createForOwner(event.userId());
        }
    }
}
