package com.vegas.sistema_gestion_operativa.users.factory;

import com.vegas.sistema_gestion_operativa.users.domain.User;
import com.vegas.sistema_gestion_operativa.users.dto.CreateUserDto;

public class UserFactory {

    public static User createFromDto(CreateUserDto user) {
        return User.builder()
                .email(user.email())
                .givenName(user.givenName())
                .familyName(user.familyName())
                .idType(user.idType())
                .phoneNumber(user.phoneNumber())
                .build();
    }
}
