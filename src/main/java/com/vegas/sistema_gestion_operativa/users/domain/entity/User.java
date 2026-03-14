package com.vegas.sistema_gestion_operativa.users.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Filter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@ToString
@Filter(name = "franchiseFilter", condition = "franchise_id = :franchiseId")
public class User {

    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String givenName;

    @Column(nullable = false)
    private String familyName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IdType idType;

    @Column(nullable = false)
    private String idNumber;

    @Column()
    private String phoneNumber;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean isActive;

    @Column(nullable = false)
    private String roleName;

    @Column(name = "franchise_id",
            columnDefinition = "BIGINT REFERENCES franchises(id)")
    private Long franchiseId;

    @Transient
    private String franchiseName;
}
