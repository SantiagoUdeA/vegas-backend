package com.vegas.sistema_gestion_operativa.unit.users.controller;

import com.vegas.sistema_gestion_operativa.users.controller.UserController;
import com.vegas.sistema_gestion_operativa.users.domain.User;
import com.vegas.sistema_gestion_operativa.users.dto.CreateUserDto;
import com.vegas.sistema_gestion_operativa.unit.users.factory.FakeUserFactory;
import com.vegas.sistema_gestion_operativa.users.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserController.
 * Tests controller logic without Spring context (no integration, no MVC).
 */
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_shouldReturnListOfUsers() {
        // Arrange
        User user1 = FakeUserFactory.createFakeUser();
        User user2 = FakeUserFactory.createFakeUser();
        List<User> expectedUsers = Arrays.asList(user1, user2);
        when(userService.findAll()).thenReturn(expectedUsers);

        // Act
        List<User> result = userController.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedUsers, result);
        verify(userService, times(1)).findAll();
    }

    @Test
    void findAll_shouldReturnEmptyListWhenNoUsers() {
        // Arrange
        when(userService.findAll()).thenReturn(List.of());

        // Act
        List<User> result = userController.findAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userService, times(1)).findAll();
    }

    @Test
    void createUser_shouldReturnCreatedUser() {
        // Arrange
        CreateUserDto dto = FakeUserFactory.createFakeCreateUserDto();
        User expectedUser = FakeUserFactory.createFakeUser();
        when(userService.create(dto)).thenReturn(expectedUser);

        // Act
        User result = userController.createUser(dto);

        // Assert
        assertNotNull(result);
        assertEquals(expectedUser, result);
        verify(userService, times(1)).create(dto);
    }

    @Test
    void createUser_shouldDelegateToService() {
        // Arrange
        CreateUserDto dto = FakeUserFactory.createFakeCreateUserDto();
        User user = FakeUserFactory.createFakeUser();
        when(userService.create(dto)).thenReturn(user);

        // Act
        userController.createUser(dto);

        // Assert
        verify(userService, times(1)).create(dto);
        verifyNoMoreInteractions(userService);
    }
}

