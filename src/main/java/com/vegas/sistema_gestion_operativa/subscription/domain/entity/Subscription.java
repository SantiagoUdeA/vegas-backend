package com.vegas.sistema_gestion_operativa.subscription.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "subscriptions")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "owner_user_id", nullable = false, unique = true,
            columnDefinition = "VARCHAR(255) REFERENCES users(id)")
    private String ownerUserId;

    @Column(name = "max_franchises", nullable = false)
    private int maxFranchises;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
