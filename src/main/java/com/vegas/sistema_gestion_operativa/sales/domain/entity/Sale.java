package com.vegas.sistema_gestion_operativa.sales.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
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

    @Column(name = "total", precision = 14, scale = 2, nullable = false)
    private BigDecimal total;

    // employee is a user in the system; user id is stored as VARCHAR
    @Column(name = "employee_id", nullable = false, updatable = false, columnDefinition = "VARCHAR(255) REFERENCES users(id)")
    private String employeeId;

    @Column(name = "branch_id", nullable = false, updatable = false, columnDefinition = "BIGINT REFERENCES branches(id)")
    private Long branchId;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<SaleDetail> details;
}

