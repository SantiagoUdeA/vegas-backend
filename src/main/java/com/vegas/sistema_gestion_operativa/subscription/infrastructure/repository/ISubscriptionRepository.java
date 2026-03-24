package com.vegas.sistema_gestion_operativa.subscription.infrastructure.repository;

import com.vegas.sistema_gestion_operativa.subscription.domain.entity.Subscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ISubscriptionRepository extends JpaRepository<Subscription, Long> {

    Optional<Subscription> findByOwnerUserId(String ownerUserId);

    boolean existsByOwnerUserId(String ownerUserId);

    Page<Subscription> findAll(Pageable pageable);
}
