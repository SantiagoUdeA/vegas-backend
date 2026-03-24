package com.vegas.sistema_gestion_operativa.raw_material.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RawMaterialCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(columnDefinition = "boolean default true")
    private boolean active;

    @Column(name = "branch_id", updatable = false, nullable = false,
            columnDefinition = "BIGINT REFERENCES branches(id)")
    private Long branchId;

    public void deactivate() {
        this.active = false;
    }
}
