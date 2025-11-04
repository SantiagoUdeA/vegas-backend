package com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RawMaterialInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK hacia tabla raw_material (sin relación JPA)
    @Column(
            name = "raw_material_id",
            nullable = false,
            updatable = false,
            columnDefinition = "BIGINT REFERENCES raw_material(id)"
    )
    private Long rawMaterialId;

    // FK hacia tabla site (sin relación JPA)
    @Column(
            name = "branch_id",
            nullable = false,
            updatable = false,
            columnDefinition = "BIGINT REFERENCES branches(id)"
    )
    private Long branchId;

    @Column(nullable = false, columnDefinition = "DOUBLE PRECISION DEFAULT 0")
    private Double currentStock;

    @Column
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}