package com.vegas.sistema_gestion_operativa.users.service;

import com.vegas.sistema_gestion_operativa.users.domain.User;
import com.vegas.sistema_gestion_operativa.users.dto.CreateUserDto;
import com.vegas.sistema_gestion_operativa.users.repository.IUserRepository;
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

    @InjectMocks
    private UserService userService;

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
    void create_shouldBuildUserFromDtoAndSave() {
        CreateUserDto dto = new CreateUserDto("new@example.com", "New", "User", "CC", "555");

        // Simular que el repositorio devuelve exactamente el objeto que se le pasa
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.create(dto);

        assertNotNull(result);
        assertEquals(dto.email(), result.getEmail());
        assertEquals(dto.givenName(), result.getGivenName());
        assertEquals(dto.familyName(), result.getFamilyName());
        assertEquals(dto.idType(), result.getIdType());
        assertEquals(dto.phoneNumber(), result.getPhoneNumber());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void create_whenRepositoryThrows_shouldPropagateException() {
        CreateUserDto dto = new CreateUserDto("err@example.com", "Err", "User", "CC", "999");
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("DB failure"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.create(dto));
        assertEquals("DB failure", ex.getMessage());
        verify(userRepository, times(1)).save(any(User.class));
    }
}

