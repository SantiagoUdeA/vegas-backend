package com.vegas.sistema_gestion_operativa.unit.users.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vegas.sistema_gestion_operativa.security.config.CustomPermissionEvaluator;
import com.vegas.sistema_gestion_operativa.users.controller.UserController;
import com.vegas.sistema_gestion_operativa.users.domain.User;
import com.vegas.sistema_gestion_operativa.users.dto.CreateUserDto;
import com.vegas.sistema_gestion_operativa.users.factory.FakeUserFactory;
import com.vegas.sistema_gestion_operativa.users.service.UserService;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for UserController with validation.
 * Uses MockMvc to test HTTP layer including validation.
 */
@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private CustomPermissionEvaluator customPermissionEvaluator;

    private static final Faker faker = new Faker();
    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = FakeUserFactory.createFakeUser();
        // Configurar el CustomPermissionEvaluator para que siempre retorne true en los tests
        when(customPermissionEvaluator.hasPermission(any(), any(), any())).thenReturn(true);
        when(customPermissionEvaluator.hasPermission(any(), any(), any(), any())).thenReturn(true);
    }

    @Test
    @WithMockUser(authorities = "USERS_CREATE")
    void createUser_whenDtoIsValid_shouldReturnCreatedUser() throws Exception {
        CreateUserDto validDto = FakeUserFactory.createFakeCreateUserDto();
        when(userService.create(any(CreateUserDto.class))).thenReturn(mockUser);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(mockUser.getEmail()));

        verify(userService).create(any(CreateUserDto.class));
    }

    @Test
    @WithMockUser(authorities = "USERS_CREATE")
    void createUser_whenEmailIsNull_shouldReturnBadRequest() throws Exception {
        CreateUserDto invalidDto = FakeUserFactory.createFakeCreateUserDtoWithNullEmail();

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").exists());

        verify(userService, never()).create(any(CreateUserDto.class));
    }

    @Test
    @WithMockUser(authorities = "USERS_CREATE")
    void createUser_whenEmailIsInvalid_shouldReturnBadRequest() throws Exception {
        CreateUserDto invalidDto = FakeUserFactory.createFakeCreateUserDtoWithInvalidEmail();

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").exists());

        verify(userService, never()).create(any(CreateUserDto.class));
    }

    @Test
    @WithMockUser(authorities = "USERS_CREATE")
    void createUser_whenGivenNameIsNull_shouldReturnBadRequest() throws Exception {
        CreateUserDto invalidDto = FakeUserFactory.createFakeCreateUserDtoWithNullGivenName();

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.givenName").exists());

        verify(userService, never()).create(any(CreateUserDto.class));
    }

    @Test
    @WithMockUser(authorities = "USERS_CREATE")
    void createUser_whenFamilyNameIsNull_shouldReturnBadRequest() throws Exception {
        CreateUserDto invalidDto = FakeUserFactory.createFakeCreateUserDtoWithNullFamilyName();

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.familyName").exists());

        verify(userService, never()).create(any(CreateUserDto.class));
    }

    @Test
    @WithMockUser(authorities = "USERS_CREATE")
    void createUser_whenGivenNameExceedsMaxLength_shouldReturnBadRequest() throws Exception {
        CreateUserDto invalidDto = FakeUserFactory.createFakeCreateUserDtoWithLongGivenName();

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.givenName").exists());

        verify(userService, never()).create(any(CreateUserDto.class));
    }

    @Test
    @WithMockUser(authorities = "USERS_CREATE")
    void createUser_whenFamilyNameExceedsMaxLength_shouldReturnBadRequest() throws Exception {
        CreateUserDto invalidDto = FakeUserFactory.createFakeCreateUserDtoWithLongFamilyName();

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.familyName").exists());

        verify(userService, never()).create(any(CreateUserDto.class));
    }

    @Test
    @WithMockUser(authorities = "USERS_CREATE")
    void createUser_whenMultipleFieldsAreInvalid_shouldReturnBadRequestWithAllErrors() throws Exception {
        CreateUserDto invalidDto = FakeUserFactory.createFakeCreateUserDtoWithMultipleNullFields();

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.givenName").exists())
                .andExpect(jsonPath("$.familyName").exists());

        verify(userService, never()).create(any(CreateUserDto.class));
    }
}
