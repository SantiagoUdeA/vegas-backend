package com.vegas.sistema_gestion_operativa.subscription.application.factory;

import com.vegas.sistema_gestion_operativa.subscription.domain.entity.Subscription;

public class SubscriptionFactory {

    private SubscriptionFactory() {
    }

    public static Subscription createSubscription(String ownerUserId, int maxFranchises) {
        return Subscription.builder()
                .ownerUserId(ownerUserId)
                .maxFranchises(maxFranchises)
                .build();
    }
}
