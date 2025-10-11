package com.vegas.sistema_gestion_operativa.acceptance.users;

import com.vegas.sistema_gestion_operativa.roles.domain.Role;
import com.vegas.sistema_gestion_operativa.unit.users.factory.FakeUserFactory;
import com.vegas.sistema_gestion_operativa.users.domain.User;
import com.vegas.sistema_gestion_operativa.users.dto.CreateUserDto;
import com.vegas.sistema_gestion_operativa.utils.CucumberSpringConfiguration;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Step Definitions para las pruebas de aceptación del UserController.
 * Define los pasos en lenguaje Gherkin para crear y validar usuarios.
 */

@ContextConfiguration(classes = CucumberSpringConfiguration.class)
public class UserCreationSteps extends CucumberSpringConfiguration {

    private CreateUserDto userDto;
    private ResponseEntity<User> response;

    @Before
    public void setUp() {
        // Configuración antes de cada escenario si es necesaria
    }

    @Given("the administrator is authenticated")
    public void theAdministratorIsAuthenticated() {
        setJwt(() -> fakeJwtFactory.createJwtWithRole(Role.ADMIN));
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
        assertNotNull(response, "La respuesta no debería ser null");
        assertEquals(200, response.getStatusCode().value(), "El código de estado debería ser 200 OK");
        assertNotNull(response.getBody(), "El usuario creado no debería ser null");
    }

    @And("display a confirmation message {string}")
    public void displayAConfirmationMessage(String message) {
        // Implementación futura para validar mensaje de confirmación
    }

    @Given("the user’s information is already registered in the system")
    public void userAlreadyRegistered() {
        userDto = FakeUserFactory.createFakeCreateUserDto();
        testRestTemplate.postForEntity("/api/v1/users", userDto, User.class);
    }

    @When("the administrator enters the information of an already registered employee")
    public void entersRegisteredEmployeeInfo() {
        // El DTO ya está creado en el paso Given
    }

    @Then("an error message is displayed: “The user already exists”")
    public  void anErrorMessageIsDisplayed() {
        assertNotNull(response, "Answer should not be null");
        assertEquals(400, response.getStatusCode().value(), "Status code should be 400 Bad Request");
    }

    @When("incomplete user information is entered")
    public void incompleteUserInformationIsEntered() {
        userDto = new CreateUserDto(null, null, null, null, null, null, null); // DTO incompleto
        response = testRestTemplate.postForEntity("/api/v1/users", userDto, User.class);
    }

    @Then("the system displays an error message: “Required fields are missing”")
    public  void requiredFieldsAreMissing() {
        assertNotNull(response, "Answer should not be null");
        assertEquals(400, response.getStatusCode().value(), "Status code should be 400 Bad Request");
    }


}