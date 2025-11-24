package com.vegas.sistema_gestion_operativa.products_inventory.domain.entity;


import com.vegas.sistema_gestion_operativa.common.domain.Quantity;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.entity.MovementReason;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductInventoryMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "product_id",
            nullable = false,
            updatable = false,
            columnDefinition = "BIGINT REFERENCES product(id)"
    )
    private Long productId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MovementReason movementReason;

    @Column(length = 500)
    private String justification;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime movementDate;

    @Embedded
    private Quantity quantity;

    @Column(
            name = "user_id",
            nullable = false,
            updatable = false,
            columnDefinition = "VARCHAR(255) REFERENCES users(id)"
    )
    private String userId;
}
