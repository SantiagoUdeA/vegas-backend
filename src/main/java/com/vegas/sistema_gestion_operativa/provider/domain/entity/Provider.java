package com.vegas.sistema_gestion_operativa.provider.domain.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"nit", "branch_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Provider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String nit;

    private String phoneNumber;

    @Column(columnDefinition = "boolean default true")
    private boolean active;

    @Column(name = "branch_id", updatable = false, nullable = false,
            columnDefinition = "BIGINT REFERENCES branches(id)")
    private Long branchId;

    public void deactivate() {
        this.active = false;
    }
}
