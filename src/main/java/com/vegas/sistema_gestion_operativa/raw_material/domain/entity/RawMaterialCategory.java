package com.vegas.sistema_gestion_operativa.raw_material.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Filter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Filter(name = "franchiseFilter", condition = "franchise_id = :franchiseId")
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

    @Column(name = "franchise_id", updatable = false,
            columnDefinition = "BIGINT REFERENCES franchises(id)")
    private Long franchiseId;

    public void deactivate() {
        this.active = false;
    }
}
