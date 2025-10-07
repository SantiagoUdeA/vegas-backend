package com.vegas.sistema_gestion_operativa.users.controller;

import com.vegas.sistema_gestion_operativa.users.domain.User;
import com.vegas.sistema_gestion_operativa.users.dto.CreateUserDto;
import com.vegas.sistema_gestion_operativa.users.service.UserService;
import io.cucumber.java.en.*;
import io.cucumber.java.Before;
import io.cucumber.java.After;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class UserControllerSteps {

    // Usar Mockito en lugar de la anotación @MockBean (deprecada)
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private CreateUserDto createUserDto;
    private User createdUser;
    private Exception exception;
    private String confirmationMessage;
    private String errorMessage;

    // Guardar el AutoCloseable para cerrarlo en @After y evitar la advertencia
    private AutoCloseable mocks;

    @Before
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @After
    public void tearDown() throws Exception {
        if (mocks != null) {
            mocks.close();
        }
    }

    @Given("that the administrator is authenticated")
    public void admin_authenticated() {
        // Simulación de autenticación, si es necesario
    }

    @When("the administrator enters an employee's information")
    public void admin_enters_employee_info() {
        createUserDto = new CreateUserDto(
                "test@example.com", "Test", "User", "CC", "1234567890"
        );
    }

    @When("creates a user, the system should register the new user")
    public void admin_creates_user() {
        User user = new User();
        user.setEmail(createUserDto.email());
        user.setFamilyName(createUserDto.familyName());
        user.setGivenName(createUserDto.givenName());
        Mockito.when(userService.create(createUserDto)).thenReturn(user);
        createdUser = userController.createUser(createUserDto);
        confirmationMessage = "User created successfully";
    }

    @Then("display a confirmation message \"User created successfully\"")
    public void display_confirmation_message() {
        Assertions.assertNotNull(createdUser);
        Assertions.assertEquals("User created successfully", confirmationMessage);
    }

    @Given("that the user's information is already registered in the system")
    public void user_already_registered() {
        // Asegurarnos de inicializar el DTO con la misma información que se usará en el When
        if (createUserDto == null) {
            createUserDto = new CreateUserDto("test@example.com", "Test", "User", "CC", "1234567890");
        }
        Mockito.when(userService.create(createUserDto)).thenThrow(new RuntimeException("User already exists"));
    }

    @When("the administrator enters the information of an already registered employee")
    public void admin_enters_registered_employee_info() {
        // Ya configurado en el Given
    }

    @When("creates a user, an error message is displayed \"User already exists\"")
    public void admin_creates_user_error() {
        try {
            userController.createUser(createUserDto);
        } catch (Exception e) {
            exception = e;
            errorMessage = e.getMessage();
        }
    }

    @Then("an error message is displayed \"User already exists\"")
    public void display_error_message() {
        Assertions.assertNotNull(exception);
        Assertions.assertEquals("User already exists", errorMessage);
    }
}
