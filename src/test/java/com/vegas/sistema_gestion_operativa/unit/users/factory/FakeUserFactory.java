package com.vegas.sistema_gestion_operativa.unit.users.factory;

import com.vegas.sistema_gestion_operativa.users.domain.IdType;
import com.vegas.sistema_gestion_operativa.users.domain.User;
import com.vegas.sistema_gestion_operativa.users.dto.CreateUserDto;
import net.datafaker.Faker;

public class FakeUserFactory {

    private static final Faker faker = new Faker();

    public static User createFakeUser() {
        return User.builder()
                .id(faker.internet().uuid())
                .email(faker.internet().emailAddress())
                .familyName(faker.name().lastName())
                .idType(IdType.CC)
                .idNumber(faker.number().digits(8))
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .build();
    }

    /*
     * Creates a fake CreateUserDto with random but realistic data.
     * @return a CreateUserDto instance populated with fake data
     */
    public static CreateUserDto createFakeCreateUserDto() {
        return new CreateUserDto(
                faker.internet().emailAddress(),
                faker.name().firstName(),
                faker.name().lastName(),
                "CC",
                faker.number().digits(8),
                "+57" + faker.number().digits(10),
                "CASHIER"
        );
    }
}
