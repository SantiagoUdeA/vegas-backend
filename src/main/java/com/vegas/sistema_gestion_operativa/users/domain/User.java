package com.vegas.sistema_gestion_operativa.users.domain;

import com.vegas.sistema_gestion_operativa.roles.domain.Rol;
import com.vegas.sistema_gestion_operativa.branches.domain.Branch;
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
    private String phoneNumber;
    private Rol rol;
    private Branch branch;
}
