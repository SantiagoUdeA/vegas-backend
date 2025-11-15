package com.vegas.sistema_gestion_operativa.inventory.raw_material.domain.entity;

import com.vegas.sistema_gestion_operativa.common.domain.UnitOfMeasure;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RawMaterialMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UnitOfMeasure unitOfMeasure;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime movementDate;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MovementReason movementReason;

    @Column(
            name = "raw_material_batch_id",
            nullable = false,
            updatable = false,
            columnDefinition = "BIGINT REFERENCES raw_material_batch(id)"
    )
    private Long rawMaterialBatchId;

    @Column(
            name = "user_id",
            nullable = false,
            updatable = false,
            columnDefinition = "VARCHAR(255) REFERENCES users(id)"
    )
    private String userId;
}
