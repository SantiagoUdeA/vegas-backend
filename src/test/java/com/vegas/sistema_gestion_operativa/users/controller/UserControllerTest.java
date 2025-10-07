package com.vegas.sistema_gestion_operativa.users.controller;

import com.vegas.sistema_gestion_operativa.users.domain.User;
import com.vegas.sistema_gestion_operativa.users.dto.CreateUserDto;
import com.vegas.sistema_gestion_operativa.users.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private CreateUserDto dto;

    @BeforeEach
    void setUp() {
        dto = new CreateUserDto("carlos@example.com", "Carlos", "Perez", "CC", "3001234567");
    }

    @Test
    void findAll_shouldReturnListFromService() {
        User u1 = User.builder().email("a@ex.com").givenName("A").familyName("One").build();
        User u2 = User.builder().email("b@ex.com").givenName("B").familyName("Two").build();
        when(userService.findAll()).thenReturn(List.of(u1, u2));

        List<User> result = userController.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("a@ex.com", result.get(0).getEmail());
        verify(userService, times(1)).findAll();
    }

    @Test
    void createUser_shouldReturnCreatedUser() {
        User expected = User.builder()
                .email(dto.email())
                .givenName(dto.givenName())
                .familyName(dto.familyName())
                .idType(dto.idType())
                .phoneNumber(dto.phoneNumber())
                .build();

        when(userService.create(dto)).thenReturn(expected);

        User result = userController.createUser(dto);

        assertNotNull(result);
        assertEquals(expected.getEmail(), result.getEmail());
        assertEquals(expected.getGivenName(), result.getGivenName());
        verify(userService, times(1)).create(dto);
    }

    @Test
    void createUser_whenServiceThrows_shouldPropagateException() {
        when(userService.create(dto)).thenThrow(new RuntimeException("User already exists"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userController.createUser(dto));
        assertEquals("User already exists", ex.getMessage());
        verify(userService, times(1)).create(dto);
    }
}

