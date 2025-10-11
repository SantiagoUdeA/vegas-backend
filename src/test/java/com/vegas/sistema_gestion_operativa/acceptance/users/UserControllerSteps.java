package com.vegas.sistema_gestion_operativa.acceptance.users;

import com.vegas.sistema_gestion_operativa.roles.domain.Role;
import com.vegas.sistema_gestion_operativa.unit.users.factory.FakeUserFactory;
import com.vegas.sistema_gestion_operativa.users.domain.User;
import com.vegas.sistema_gestion_operativa.users.dto.CreateUserDto;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerSteps extends CucumberSpringConfiguration {

    private CreateUserDto userDto;
    private ResponseEntity<User> response;

    @Before
    public void setUp() {

    }

    @Given("the administrator is authenticated")
    public void theAdministratorIsAuthenticated() {
        setJwt(() -> fakeJwtFactory.createJwtWithRole(Role.CASHIER));
    }

    @When("the administrator enters the employee's information")
    public void theAdministratorEntersTheEmployeeInformation() {
        userDto = FakeUserFactory.createFakeCreateUserDto();
    }

    @And("creates a user")
    public void createsAUser() {
        response = testRestTemplate.postForEntity("/api/v1/users", userDto, User.class);
    }

    @Then("the system should register the new user")
    public void theSystemShouldRegisterTheNewUser() {
        assertEquals(200, response.getStatusCode().value());
    }

    @And("display a confirmation message {string}")
    public void displayAConfirmationMessage(String message) {

    }

    /*@Given("the user's information is already registered in the system")
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
    }*/
}