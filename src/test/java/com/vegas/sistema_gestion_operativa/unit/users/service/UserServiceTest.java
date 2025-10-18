/*
package com.vegas.sistema_gestion_operativa.unit.users.service;

import com.vegas.sistema_gestion_operativa.aws.service.CognitoIdentityService;
import com.vegas.sistema_gestion_operativa.roles.domain.Role;
import com.vegas.sistema_gestion_operativa.users.domain.entity.User;
import com.vegas.sistema_gestion_operativa.users.dto.CreateUserDto;
import com.vegas.sistema_gestion_operativa.unit.users.factory.FakeUserFactory;
import com.vegas.sistema_gestion_operativa.users.exceptions.UserAlreadyExistsException;
import com.vegas.sistema_gestion_operativa.users.factory.IdTypeFactory;
import com.vegas.sistema_gestion_operativa.users.factory.UserFactory;
import com.vegas.sistema_gestion_operativa.users.repository.IUserRepository;
import com.vegas.sistema_gestion_operativa.users.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private CognitoIdentityService cognitoIdentityService;

    @Mock
    private UserFactory userFactory;

    @InjectMocks
    private UserService userService;

    private final IdTypeFactory idTypeFactory = new IdTypeFactory();

    @Test
    void findAll_shouldReturnListFromRepository() {
        User u1 = User.builder().email("a@example.com").givenName("A").familyName("One").build();
        User u2 = User.builder().email("b@example.com").givenName("B").familyName("Two").build();
        when(userRepository.findAll()).thenReturn(List.of(u1, u2));

        List<User> result = userService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("a@example.com", result.getFirst().getEmail());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void create_shouldBuildUserFromDtoAndSave() throws UserAlreadyExistsException {
        CreateUserDto dto = FakeUserFactory.createFakeCreateUserDto();
        String fakeUserId = "fake-cognito-user-id-123";

        // Mock de Cognito para devolver un ID de usuario
        when(cognitoIdentityService.createUser(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(fakeUserId);

        // Mock del UserFactory para crear el usuario con el ID de Cognito
        User expectedUser = User.builder()
                .id(fakeUserId)
                .email(dto.email())
                .givenName(dto.givenName())
                .familyName(dto.familyName())
                .idType(idTypeFactory.createIdType(dto.idType()))
                .phoneNumber(dto.phoneNumber())
                .role(Role.ADMIN)
                .build();

        when(userFactory.createFromDto(eq(dto), eq(fakeUserId))).thenReturn(expectedUser);

        // Mock del repositorio para devolver el usuario guardado
        when(userRepository.save(any(User.class))).thenReturn(expectedUser);
        when(userRepository.findByEmail(dto.email())).thenReturn(java.util.Optional.empty());

        User result = userService.create(dto);

        assertNotNull(result);
        assertEquals(dto.email(), result.getEmail());
        assertEquals(dto.givenName(), result.getGivenName());
        assertEquals(dto.familyName(), result.getFamilyName());
        assertEquals(dto.idType(), result.getIdType().toString());
        assertEquals(dto.phoneNumber(), result.getPhoneNumber());
        assertEquals(fakeUserId, result.getId());

        verify(cognitoIdentityService, times(1)).createUser(
                eq(dto.email()),
                eq(dto.givenName()),
                eq(dto.familyName()),
                eq(dto.roleName()),
                eq(dto.idNumber())
        );
        verify(userFactory, times(1)).createFromDto(eq(dto), eq(fakeUserId));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void create_whenRepositoryThrows_shouldPropagateException() {
        CreateUserDto dto = FakeUserFactory.createFakeCreateUserDto();
        String fakeUserId = "fake-cognito-user-id-123";

        // Mock de Cognito para devolver un ID de usuario
        when(cognitoIdentityService.createUser(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(fakeUserId);

        // Mock del UserFactory para crear el usuario
        User expectedUser = User.builder()
                .id(fakeUserId)
                .email(dto.email())
                .givenName(dto.givenName())
                .familyName(dto.familyName())
                .build();

        when(userFactory.createFromDto(eq(dto), eq(fakeUserId))).thenReturn(expectedUser);
        when(userRepository.findByEmail(dto.email())).thenReturn(java.util.Optional.empty());
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("DB failure"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.create(dto));
        assertEquals("DB failure", ex.getMessage());
        verify(userRepository, times(1)).save(any(User.class));
    }
}
*/
