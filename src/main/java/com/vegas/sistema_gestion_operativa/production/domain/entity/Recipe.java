package com.vegas.sistema_gestion_operativa.production.domain.entity;

import com.vegas.sistema_gestion_operativa.products.domain.entity.Product;
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

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Ingredient> ingredients;

    @Column(
            name = "product_id",
            nullable = false,
            updatable = false,
            insertable = false
    )
    private Long productId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

}
