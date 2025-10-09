package com.vegas.sistema_gestion_operativa.users.factory;

import com.vegas.sistema_gestion_operativa.roles.factory.RoleFactory;
import com.vegas.sistema_gestion_operativa.users.domain.User;
import com.vegas.sistema_gestion_operativa.users.dto.CreateUserDto;

/**
 * Factory class for creating User instances from DTOs.
 */
public class UserFactory {

    /**
     * Creates a User instance from a CreateUserDto.
     * @param user DTO containing user data
     * @return User instance
     */
    public static User createFromDto(CreateUserDto user) {
        return User.builder()
                .email(user.email())
                .givenName(user.givenName())
                .familyName(user.familyName())
                .idType(user.idType())
                .phoneNumber(user.phoneNumber())
                .role(RoleFactory.createRole(user.roleName()))
                .build();
    }
}
