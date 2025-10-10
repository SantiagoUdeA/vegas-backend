package com.vegas.sistema_gestion_operativa.acceptance.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vegas.sistema_gestion_operativa.users.dto.CreateUserDto;
import com.vegas.sistema_gestion_operativa.users.repository.IUserRepository;
import com.vegas.sistema_gestion_operativa.users.factory.FakeUserFactory;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class UserCreationSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IUserRepository userRepository; // ⚡ ahora usamos el repo real

    private CreateUserDto userDto;
    private MvcResult result;

    @Given("the administrator is authenticated")
    public void theAdministratorIsAuthenticated() {
        // @WithMockUser simula al admin
    }

    @When("the administrator enters the employee's information")
    public void entersEmployeeInformation() {
        userDto = FakeUserFactory.createFakeCreateUserDto();
    }

    @And("creates a user")
    public void createsUser() throws Exception {
        result = mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto))
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").authorities(() -> "USERS_CREATE"))
                )
                .andReturn();
    }

    @Then("the system should register the new user")
    public void registerUser() throws Exception {
        assertEquals(200, result.getResponse().getStatus(), result.getResponse().getContentAsString());
    }

    @And("display a confirmation message \"User created successfully\"")
    public void displayConfirmationMessage() {
    }
}