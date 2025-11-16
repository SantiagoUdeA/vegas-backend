package com.vegas.sistema_gestion_operativa.inventory.raw_material.domain.entity;

import com.vegas.sistema_gestion_operativa.common.domain.Money;
import com.vegas.sistema_gestion_operativa.common.domain.Quantity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RawMaterialBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String batchNumber;

    @Column(nullable = false)
    @Embedded
    private Quantity quantity;

    @Embedded
    private Money totalCost;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime entryDate;

    @Column(nullable = false)
    private Date expirationDate;

    @Column(
            name = "branch_id",
            nullable = false,
            updatable = false,
            columnDefinition = "BIGINT REFERENCES branches(id)"
    )
    private Long branchId;

    @Column(
            name = "raw_material_id",
            nullable = false,
            updatable = false,
            columnDefinition = "BIGINT REFERENCES raw_material(id)"
    )
    private Long rawMaterialId;

    @Column(
            name = "provider_id",
            nullable = false,
            updatable = false,
            columnDefinition = "BIGINT REFERENCES provider(id)"
    )
    private Long providerId;
}
