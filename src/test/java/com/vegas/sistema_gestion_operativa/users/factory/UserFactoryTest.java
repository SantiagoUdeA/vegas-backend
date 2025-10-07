package com.vegas.sistema_gestion_operativa.users.factory;

import com.vegas.sistema_gestion_operativa.users.domain.User;
import com.vegas.sistema_gestion_operativa.users.dto.CreateUserDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserFactoryTest {

    @Test
    void createFromDto_shouldMapAllFields() {
        CreateUserDto dto = new CreateUserDto("john.doe@example.com", "John", "Doe", "CC", "123456789");

        User user = UserFactory.createFromDto(dto);

        assertNotNull(user);
        assertEquals(dto.email(), user.getEmail());
        assertEquals(dto.givenName(), user.getGivenName());
        assertEquals(dto.familyName(), user.getFamilyName());
        assertEquals(dto.idType(), user.getIdType());
        assertEquals(dto.phoneNumber(), user.getPhoneNumber());
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    void createFromDto_nullDto_shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> UserFactory.createFromDto(null));
    }
}
