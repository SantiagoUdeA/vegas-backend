package com.vegas.sistema_gestion_operativa.production.domain.entity;

import com.vegas.sistema_gestion_operativa.common.domain.Quantity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "production")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Production {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false, columnDefinition = "BIGINT REFERENCES product(id)")
    private Long productId;

    @Column(name = "recipe_id", nullable = false, columnDefinition = "BIGINT REFERENCES recipe(id)")
    private Long recipeId;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "quantity_produced", precision = 19, scale = 4, nullable = false)
    )
    private Quantity quantityProduced;

    @Column(name = "production_date", nullable = false)
    private LocalDateTime productionDate;

    @Column(name = "branch_id", nullable = false, columnDefinition = "BIGINT REFERENCES branches(id)")
    private Long branchId;

    @Column(name = "user_id", nullable = false, columnDefinition = "VARCHAR(255) REFERENCES users(id)")
    private String userId;

    @Column(name = "observations", columnDefinition = "TEXT")
    private String observations;
}
