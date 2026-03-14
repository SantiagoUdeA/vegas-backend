package com.vegas.sistema_gestion_operativa.provider.domain.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Filter;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"nit", "franchise_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Filter(name = "franchiseFilter", condition = "franchise_id = :franchiseId")
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

    @Column(name = "franchise_id", updatable = false,
            columnDefinition = "BIGINT REFERENCES franchises(id)")
    private Long franchiseId;

    public void deactivate() {
        this.active = false;
    }
}
