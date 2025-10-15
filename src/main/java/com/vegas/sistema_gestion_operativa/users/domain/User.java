package com.vegas.sistema_gestion_operativa.users.domain;

import com.vegas.sistema_gestion_operativa.branches.domain.Branch;
import com.vegas.sistema_gestion_operativa.roles.domain.Role;
import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a user in the system.
 * Contains user details such as email, name, ID type, phone number, role, and branch.
 */
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
    @Column(unique = true)
    private String idNumber;
    @Column()
    private String phoneNumber;
    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean isActive;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;
}
