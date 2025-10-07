package com.vegas.sistema_gestion_operativa.users.factory;

import com.vegas.sistema_gestion_operativa.users.domain.User;
import com.vegas.sistema_gestion_operativa.users.dto.CreateUserDto;
import net.datafaker.Faker;

public class FakeUserFactory {

    private static final Faker faker = new Faker();

    public static User createFakeUser() {
        return User.builder()
                .id(faker.internet().uuid())
                .email(faker.internet().emailAddress())
                .givenName(faker.name().firstName())
                .familyName(faker.name().lastName())
                .idType("CC")
                .idNumber(faker.number().digits(8))
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .build();
    }

    public static CreateUserDto createFakeCreateUserDto() {
        return new CreateUserDto(
                faker.internet().emailAddress(),
                faker.name().firstName(),
                faker.name().lastName(),
                "CC",
                faker.number().digits(8),
                faker.phoneNumber().phoneNumber()
        );
    }
}
