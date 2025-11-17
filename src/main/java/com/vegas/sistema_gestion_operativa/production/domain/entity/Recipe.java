package com.vegas.sistema_gestion_operativa.production.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer unitsProduced;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private Boolean active;

    @Column()
    private String observations;

    @OneToMany(mappedBy = "recipeId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ingredient> ingredients;

    @Column(
            name = "product_id",
            nullable = false,
            updatable = false,
            columnDefinition = "BIGINT REFERENCES product(id)"
    )
    private Long productId;

}
