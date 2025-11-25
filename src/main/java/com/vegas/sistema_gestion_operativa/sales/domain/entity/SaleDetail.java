package com.vegas.sistema_gestion_operativa.sales.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.vegas.sistema_gestion_operativa.common.domain.Money;
import com.vegas.sistema_gestion_operativa.common.domain.Quantity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sale_detail")
public class SaleDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_id")
    private Long id;

    @Embedded
    @Column(name = "quantity", nullable = false)
    private Quantity quantity;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "unit_price", precision = 19, scale = 4, nullable = false)
    )
    private Money unitPrice;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "subtotal", precision = 19, scale = 4, nullable = false)
    )
    private Money subtotal;

    @Column(name = "product_id", nullable = false, updatable = false, columnDefinition = "BIGINT REFERENCES product(id)")
    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id")
    @JsonBackReference
    private Sale sale;
}

