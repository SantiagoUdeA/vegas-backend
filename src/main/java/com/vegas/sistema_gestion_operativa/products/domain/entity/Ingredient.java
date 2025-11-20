package com.vegas.sistema_gestion_operativa.products.domain.entity;

import com.vegas.sistema_gestion_operativa.raw_material.domain.entity.RawMaterial;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @Column(
            name = "raw_material_id",
            nullable = false,
            insertable = false,
            updatable = false
    )
    private Long rawMaterialId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "raw_material_id", nullable = false)
    private RawMaterial rawMaterial;

}
