// src/main/java/com/vegas/sistema_gestion_operativa/users/UserDto.java
package com.vegas.sistema_gestion_operativa.users.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vegas.sistema_gestion_operativa.users.domain.entity.IdType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private String id;
    private String email;
    private String givenName;
    private String familyName;
    private IdType idType;
    private String idNumber;
    private String phoneNumber;
    private boolean isActive;
    private String role;
    private Set<String> branchIds = new HashSet<>();
}
