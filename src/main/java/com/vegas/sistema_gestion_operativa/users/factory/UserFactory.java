package com.vegas.sistema_gestion_operativa.users.factory;

import com.vegas.sistema_gestion_operativa.roles.factory.RoleFactory;
import com.vegas.sistema_gestion_operativa.users.domain.User;
import com.vegas.sistema_gestion_operativa.users.dto.CreateUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Factory class for creating User instances from DTOs.
 */
@Component
public class UserFactory {

    private final RoleFactory roleFactory;

    @Autowired
    public UserFactory(RoleFactory roleFactory) {
        this.roleFactory = roleFactory;
    }

    /**
     * Creates a User instance from a CreateUserDto.
     * @param user DTO containing user data
     * @return User instance
     */
    public User createFromDto(CreateUserDto user) {
        return User.builder()
                .email(user.email())
                .givenName(user.givenName())
                .familyName(user.familyName())
                .idType(user.idType())
                .phoneNumber(user.phoneNumber())
                .role(roleFactory.createRole(user.roleName()))
                .build();
    }

    public User createFromDto(CreateUserDto user, String id) {
        return User.builder()
                .id(id)
                .email(user.email())
                .givenName(user.givenName())
                .familyName(user.familyName())
                .idType(user.idType())
                .phoneNumber(user.phoneNumber())
                .role(roleFactory.createRole(user.roleName()))
                .build();
    }
}
