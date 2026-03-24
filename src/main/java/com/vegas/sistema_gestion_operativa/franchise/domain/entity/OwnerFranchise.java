package com.vegas.sistema_gestion_operativa.franchise.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "owner_franchises", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"owner_id", "franchise_id"})
})
public class OwnerFranchise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "owner_id", nullable = false)
    private String ownerId;

    @Column(name = "franchise_id", nullable = false,
            columnDefinition = "BIGINT REFERENCES franchises(id)")
    private Long franchiseId;
}
