package com.vegas.sistema_gestion_operativa.production.domain.entity;

import com.vegas.sistema_gestion_operativa.common.domain.Quantity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "quantity", precision = 19, scale = 4)
    )
    private Quantity quantity;

    @Column()
    private String observations;

    @Column(nullable = false)
    private Long recipeId;

    @Column(
            name = "raw_material_id",
            nullable = false,
            updatable = false,
            columnDefinition = "BIGINT REFERENCES raw_material(id)"
    )
    private Long rawMaterialId;

}
