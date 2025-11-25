package com.vegas.sistema_gestion_operativa.users.domain.events;

import org.jmolecules.event.annotation.DomainEvent;

import java.util.Optional;

@DomainEvent
public record UserCreatedEvent(String userId, Optional<Long> branchId, String roleName) {
}
