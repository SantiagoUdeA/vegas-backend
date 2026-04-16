package com.vegas.sistema_gestion_operativa.franchise.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "franchises")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Franchise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String slug;

    @Column(columnDefinition = "boolean default true")
    private boolean active;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public void deactivate() {
        this.active = false;
    }
}
