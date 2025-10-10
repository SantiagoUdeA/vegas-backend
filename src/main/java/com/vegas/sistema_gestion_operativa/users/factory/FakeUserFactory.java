package com.vegas.sistema_gestion_operativa.users.factory;

import com.vegas.sistema_gestion_operativa.users.domain.User;
import com.vegas.sistema_gestion_operativa.users.dto.CreateUserDto;
import net.datafaker.Faker;

import java.math.BigInteger;

public class FakeUserFactory {

    private static final Faker faker = new Faker();

    public static User createFakeUser() {
        return User.builder()
                .id(new BigInteger(faker.number().digits(5)))
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
                "+57" + faker.number().digits(10),
                "CASHIER"
        );
    }

    /**
     * Crea un CreateUserDto con email null para probar validación.
     * @return CreateUserDto con email null
     */
    public static CreateUserDto createFakeCreateUserDtoWithNullEmail() {
        return new CreateUserDto(
                null,
                faker.name().firstName(),
                faker.name().lastName(),
                "CC",
                faker.number().digits(8),
                faker.phoneNumber().phoneNumber(),
                "CASHIER"
        );
    }

    /**
     * Crea un CreateUserDto con email en formato inválido para probar validación.
     * @return CreateUserDto con email inválido
     */
    public static CreateUserDto createFakeCreateUserDtoWithInvalidEmail() {
        return new CreateUserDto(
                "invalid-email-format",
                faker.name().firstName(),
                faker.name().lastName(),
                "CC",
                faker.number().digits(8),
                faker.phoneNumber().phoneNumber(),
                "CASHIER"
        );
    }

    /**
     * Crea un CreateUserDto con givenName null para probar validación.
     * @return CreateUserDto con givenName null
     */
    public static CreateUserDto createFakeCreateUserDtoWithNullGivenName() {
        return new CreateUserDto(
                faker.internet().emailAddress(),
                null,
                faker.name().lastName(),
                "CC",
                faker.number().digits(8),
                faker.phoneNumber().phoneNumber(),
                "CASHIER"
        );
    }

    /**
     * Crea un CreateUserDto con familyName null para probar validación.
     * @return CreateUserDto con familyName null
     */
    public static CreateUserDto createFakeCreateUserDtoWithNullFamilyName() {
        return new CreateUserDto(
                faker.internet().emailAddress(),
                faker.name().firstName(),
                null,
                "CC",
                faker.number().digits(8),
                faker.phoneNumber().phoneNumber(),
                "CASHIER"
        );
    }

    /**
     * Crea un CreateUserDto con givenName que excede el máximo permitido para probar validación.
     * @return CreateUserDto con givenName muy largo (101 caracteres)
     */
    public static CreateUserDto createFakeCreateUserDtoWithLongGivenName() {
        return new CreateUserDto(
                faker.internet().emailAddress(),
                "A".repeat(101),
                faker.name().lastName(),
                "CC",
                faker.number().digits(8),
                faker.phoneNumber().phoneNumber(),
                "CASHIER"
        );
    }

    /**
     * Crea un CreateUserDto con familyName que excede el máximo permitido para probar validación.
     * @return CreateUserDto con familyName muy largo (101 caracteres)
     */
    public static CreateUserDto createFakeCreateUserDtoWithLongFamilyName() {
        return new CreateUserDto(
                faker.internet().emailAddress(),
                faker.name().firstName(),
                "B".repeat(101),
                "CC",
                faker.number().digits(8),
                faker.phoneNumber().phoneNumber(),
                "CASHIER"
        );
    }

    /**
     * Crea un CreateUserDto con múltiples campos null para probar validación.
     * @return CreateUserDto con email, givenName y familyName null
     */
    public static CreateUserDto createFakeCreateUserDtoWithMultipleNullFields() {
        return new CreateUserDto(
                null,
                null,
                null,
                "CC",
                faker.number().digits(8),
                faker.phoneNumber().phoneNumber(),
                "CASHIER"
        );
    }
}
