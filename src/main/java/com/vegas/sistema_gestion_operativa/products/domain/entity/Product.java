package com.vegas.sistema_gestion_operativa.products.domain.entity;

import com.vegas.sistema_gestion_operativa.common.domain.Money;
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

    @ManyToOne
    @JoinColumn(name = "category_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private ProductCategory category;

    @Column(columnDefinition = "boolean default true")
    private boolean active;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "price", precision = 19, scale = 4, nullable = false)
    )
    private Money price;

    @OneToOne(mappedBy = "product", fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Recipe recipe;

    @Column(
            name = "branch_id",
            // TODO uncomment this line nullable = false,
            updatable = false,
            columnDefinition = "BIGINT REFERENCES branches(id)"
    )
    private Long branchId;

    public void deactivate() {
        this.active = false;
    }
}
