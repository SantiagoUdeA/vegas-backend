package com.vegas.sistema_gestion_operativa.subscription;

import com.vegas.sistema_gestion_operativa.subscription.domain.exception.FranchiseLimitExceededException;
import com.vegas.sistema_gestion_operativa.subscription.domain.exception.NoActiveSubscriptionException;

/**
 * API inter-modulo para validacion de limites de suscripcion.
 * Usado por FranchiseService para validar creacion de franquicias.
 */
public interface ISubscriptionApi {

    /**
     * Valida si el owner puede crear una nueva franquicia.
     *
     * @param ownerUserId           ID del usuario OWNER
     * @param currentFranchiseCount numero actual de franquicias del owner
     * @throws NoActiveSubscriptionException   si el owner no tiene subscription activa
     * @throws FranchiseLimitExceededException si se excede el limite de franquicias
     */
    void validateFranchiseLimit(String ownerUserId, int currentFranchiseCount)
            throws NoActiveSubscriptionException, FranchiseLimitExceededException;

    /**
     * Obtiene el limite maximo de franquicias para un owner.
     *
     * @param ownerUserId ID del usuario OWNER
     * @return limite maximo de franquicias
     * @throws NoActiveSubscriptionException si el owner no tiene subscription activa
     */
    int getMaxFranchises(String ownerUserId) throws NoActiveSubscriptionException;
}
