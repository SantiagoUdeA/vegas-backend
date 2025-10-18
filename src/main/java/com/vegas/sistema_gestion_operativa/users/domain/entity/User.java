package com.vegas.sistema_gestion_operativa.users.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@ToString
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
    private String role;
}
