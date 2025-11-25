package com.vegas.sistema_gestion_operativa.sales.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vegas.sistema_gestion_operativa.common.domain.Money;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sale")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_id")
    private Long id;

    @Column(name = "sale_date", nullable = false)
    private LocalDateTime saleDate;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "total", precision = 19, scale = 4, nullable = false)
    )
    private Money total;

    @Column(name = "employee_id", nullable = false, updatable = false, columnDefinition = "VARCHAR(255) REFERENCES users(id)")
    private String employeeId;

    @Column(name = "branch_id", nullable = false, updatable = false, columnDefinition = "BIGINT REFERENCES branches(id)")
    private Long branchId;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<SaleDetail> details;
}

