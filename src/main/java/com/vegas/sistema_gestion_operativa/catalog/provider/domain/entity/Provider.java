package com.vegas.sistema_gestion_operativa.catalog.provider.domain.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
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

    @Column(nullable = false, unique = true)
    private String nit;

    private String phoneNumber;

    @Column(columnDefinition = "boolean default true")
    private boolean active;

    public void deactivate() {
        this.active = false;
    }
}
