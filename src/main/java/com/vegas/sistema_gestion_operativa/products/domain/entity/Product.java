package com.vegas.sistema_gestion_operativa.products.domain.entity;

import com.vegas.sistema_gestion_operativa.production.domain.entity.Recipe;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private ProductCategory category;

    @Column(columnDefinition = "boolean default true")
    private boolean active;

    @OneToOne(mappedBy = "product", fetch = FetchType.LAZY)
    private Recipe recipe;

    public void deactivate() {
        this.active = false;
    }
}
