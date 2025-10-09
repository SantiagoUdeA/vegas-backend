package com.vegas.sistema_gestion_operativa.users.domain;

import com.vegas.sistema_gestion_operativa.branches.domain.Branch;
import com.vegas.sistema_gestion_operativa.roles.domain.Role;
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
public class User {

    private String id;
    private String email;
    private String givenName;
    private String familyName;
    private String idType;
    private String idNumber;
    private String phoneNumber;
    private Role role;
    private Branch branch;
}
