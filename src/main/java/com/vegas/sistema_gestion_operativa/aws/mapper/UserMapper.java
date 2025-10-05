package com.vegas.sistema_gestion_operativa.aws.mapper;

import com.vegas.sistema_gestion_operativa.aws.dto.UserDto;
import org.mapstruct.Mapper;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserType;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    default UserDto toUserDto(UserType user) {
        String email = user.attributes().stream()
                .filter(attr -> "email".equals(attr.name()))
                .map(AttributeType::value)
                .findFirst()
                .orElse(null);

        String givenName = user.attributes().stream()
                .filter(attr -> "given_name".equals(attr.name()))
                .map(AttributeType::value)
                .findFirst()
                .orElse(null);

        String familyName = user.attributes().stream()
                .filter(attr -> "family_name".equals(attr.name()))
                .map(AttributeType::value)
                .findFirst()
                .orElse(null);

        return new UserDto(
                user.username(),
                email,
                givenName,
                familyName,
                user.userStatus().toString(),
                user.enabled(),
                user.userCreateDate(),
                user.userLastModifiedDate(),
                List.of() // Si necesitas grupos, requiere otra llamada a AWS
        );
    }
}
