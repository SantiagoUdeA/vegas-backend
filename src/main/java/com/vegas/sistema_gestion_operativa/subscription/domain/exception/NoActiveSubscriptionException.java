package com.vegas.sistema_gestion_operativa.subscription.domain.exception;

import com.vegas.sistema_gestion_operativa.common.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class NoActiveSubscriptionException extends ApiException {
    public NoActiveSubscriptionException(String ownerUserId) {
        super("El usuario con ID " + ownerUserId + " no tiene una suscripción activa.", HttpStatus.FORBIDDEN);
    }
}
