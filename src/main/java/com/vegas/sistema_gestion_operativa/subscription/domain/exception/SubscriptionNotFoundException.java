package com.vegas.sistema_gestion_operativa.subscription.domain.exception;

import com.vegas.sistema_gestion_operativa.common.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class SubscriptionNotFoundException extends ApiException {
    public SubscriptionNotFoundException(String ownerUserId) {
        super("Suscripción no encontrada para el usuario con ID: " + ownerUserId, HttpStatus.NOT_FOUND);
    }
}
