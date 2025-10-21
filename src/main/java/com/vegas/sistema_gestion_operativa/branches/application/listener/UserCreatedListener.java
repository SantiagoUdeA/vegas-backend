package com.vegas.sistema_gestion_operativa.branches.application.listener;

import com.vegas.sistema_gestion_operativa.branches.application.service.BranchService;
import com.vegas.sistema_gestion_operativa.users.domain.events.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class UserCreatedListener {

    private final BranchService branchService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleUserCreated(UserCreatedEvent event) {
        System.out.println("UserCreatedListener triggered for userId: " + event.userId());
        if(event.branchId().isPresent())
            branchService.assignUserToBranch(event.userId(), event.branchId().get(), false);
    }
}
