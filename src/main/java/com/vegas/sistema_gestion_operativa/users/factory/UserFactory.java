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
    private final IdTypeFactory idTypeFactory;

    @Autowired
    public UserFactory(RoleFactory roleFactory, IdTypeFactory idTypeFactory) {
        this.roleFactory = roleFactory;
        this.idTypeFactory = idTypeFactory;
    }


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
                .idType(idTypeFactory.createIdType(user.idType()))
                .idNumber(user.idNumber())
                .phoneNumber(user.phoneNumber())
                .role(roleFactory.createRole(user.roleName()))
                .build();
    }
}
