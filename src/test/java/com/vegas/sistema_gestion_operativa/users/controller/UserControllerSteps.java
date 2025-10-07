package com.vegas.sistema_gestion_operativa.users.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vegas.sistema_gestion_operativa.security.config.CustomPermissionEvaluator;
import com.vegas.sistema_gestion_operativa.users.domain.User;
import com.vegas.sistema_gestion_operativa.users.dto.CreateUserDto;
import com.vegas.sistema_gestion_operativa.users.factory.FakeUserFactory;
import com.vegas.sistema_gestion_operativa.users.service.UserService;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.junit.jupiter.api.Assertions.*;

public class UserControllerSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomPermissionEvaluator customPermissionEvaluator;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateUserDto userDto;
    private MvcResult result;
    private User fakeUser;
    private boolean mockAlreadyConfigured = false;

    @Before
    public void setUp() {
        if (userService != null) {
            reset(userService);
        }
        if (customPermissionEvaluator != null) {
            reset(customPermissionEvaluator);
            // Configurar el CustomPermissionEvaluator para que siempre retorne true en los tests
            when(customPermissionEvaluator.hasPermission(any(), any(), any())).thenReturn(true);
            when(customPermissionEvaluator.hasPermission(any(), any(), any(), any())).thenReturn(true);
        }
        fakeUser = FakeUserFactory.createFakeUser();
        userDto = null;
        result = null;
        mockAlreadyConfigured = false;
    }

    @Given("the administrator is authenticated")
    public void theAdministratorIsAuthenticated() {
        // El contexto de seguridad se configura con @WithMockUser en los métodos que lo necesitan
    }

    @When("the administrator enters the employee's information")
    public void theAdministratorEntersTheEmployeeInformation() {
        userDto = FakeUserFactory.createFakeCreateUserDto();
    }

    @And("creates a user")
    @WithMockUser(authorities = "USERS_CREATE")
    public void createsAUser() throws Exception {
        // Solo configurar el mock para éxito si no ha sido configurado previamente
        if (!mockAlreadyConfigured) {
            when(userService.create(any(CreateUserDto.class))).thenReturn(fakeUser);
        }

        String jsonContent = objectMapper.writeValueAsString(userDto);

        result = mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent)
                        .with(csrf()))
                .andReturn();
    }

    @Then("the system should register the new user")
    public void theSystemShouldRegisterTheNewUser() throws Exception {
        assertEquals(200, result.getResponse().getStatus());
        assertNotNull(result.getResponse().getContentAsString());
    }

    @And("display a confirmation message {string}")
    public void displayAConfirmationMessage(String message) {
        assertNotNull(result);
        assertEquals(200, result.getResponse().getStatus());
    }

    @Given("the user's information is already registered in the system")
    public void theUserInformationIsAlreadyRegistered() {
        // No configurar el mock aquí, se configurará cuando se intente crear el usuario
    }

    @When("the administrator enters the information of an already registered employee")
    public void theAdministratorEntersInformationOfAlreadyRegisteredEmployee() {
        userDto = FakeUserFactory.createFakeCreateUserDto();
        // Configurar el mock para que lance excepción cuando se intente crear este usuario
        when(userService.create(any(CreateUserDto.class)))
                .thenThrow(new RuntimeException("User already exists"));
        mockAlreadyConfigured = true; // Marcar que el mock ya está configurado
    }

    @Then("an error message is displayed {string}")
    @WithMockUser(authorities = "USERS_CREATE")
    public void anErrorMessageIsDisplayed(String errorMessage) throws Exception {
        // No configurar el mock aquí, ya está configurado en el paso anterior
        String jsonContent = objectMapper.writeValueAsString(userDto);

        result = mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent)
                        .with(csrf()))
                .andReturn();

        int statusCode = result.getResponse().getStatus();
        String responseBody = result.getResponse().getContentAsString();

        assertTrue(statusCode >= 400,
                "Expected status >= 400 but got " + statusCode + ". Response: " + responseBody);
    }

    @When("the administrator enters incomplete user information")
    public void theAdministratorEntersIncompleteUserInformation() {
        // Usar el factory para crear un DTO con múltiples campos null
        userDto = FakeUserFactory.createFakeCreateUserDtoWithMultipleNullFields();
    }

    @Then("the system displays an error message {string}")
    @WithMockUser(authorities = "USERS_CREATE")
    public void theSystemDisplaysAnErrorMessage(String errorMessage) throws Exception {
        String jsonContent = objectMapper.writeValueAsString(userDto);

        result = mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent)
                        .with(csrf()))
                .andReturn();

        assertTrue(result.getResponse().getStatus() >= 400);
    }
}