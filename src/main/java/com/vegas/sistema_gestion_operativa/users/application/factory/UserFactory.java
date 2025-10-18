package com.vegas.sistema_gestion_operativa.users.application.factory;

import com.vegas.sistema_gestion_operativa.users.domain.entity.User;
import com.vegas.sistema_gestion_operativa.users.application.dto.CreateUserDto;
import org.springframework.stereotype.Component;

/**
 * Factory class for creating User instances from DTOs.
 */
@Component
public class UserFactory {

    /**
     * Creates a User entity from a CreateUserDto and a given ID.
     * @param user the CreateUserDto containing user details
     * @param id the unique identifier for the user
     * @return a User entity
     */
    public User createFromDto(CreateUserDto user, String id) {
        return User.builder()
                .id(id)
                .email(user.email())
                .givenName(user.givenName())
                .familyName(user.familyName())
                .idType(user.idType())
                .idNumber(user.idNumber())
                .phoneNumber(user.phoneNumber())
                .role(user.roleName())
                .build();
    }
}
