package com.vegas.sistema_gestion_operativa.users.controller;

import com.vegas.sistema_gestion_operativa.users.domain.User;
import com.vegas.sistema_gestion_operativa.users.dto.CreateUserDto;
import com.vegas.sistema_gestion_operativa.users.dto.UpdateUserDto;
import com.vegas.sistema_gestion_operativa.users.exceptions.UserAlreadyExistsException;
import com.vegas.sistema_gestion_operativa.users.exceptions.UserAlreadyInactiveException;
import com.vegas.sistema_gestion_operativa.users.exceptions.UserNotFoundException;
import com.vegas.sistema_gestion_operativa.users.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for user management.
 * Provides endpoints to list and create users.
 */
@RestController
public class UserController {

    /**
     * Service for user operations.
     */
    private final UserService userService;

    /**
     * Constructor that injects the user service.
     * @param userService user service
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves the list of all users.
     * Requires USERS_VIEW permission.
     * @return list of users
     */
    @GetMapping("api/v1/users")
    @PreAuthorize("hasPermission(null, 'USERS_VIEW')")
    public List<User> findAll() {
        return userService.findAll();
    }

    /**
     * Creates a new user.
     * Requires USERS_CREATE permission.
     * @param dto data to create the user
     * @return created user
     */
    @PostMapping("api/v1/users")
    @PreAuthorize("hasPermission(null, 'USERS_CREATE')")
    public User createUser(@RequestBody @Valid CreateUserDto dto) throws UserAlreadyExistsException {
        System.out.println(dto.idNumber());
        return userService.create(dto);
    }

    @PatchMapping("api/v1/users/{id}")
    @PreAuthorize("hasPermission(null, 'USERS_EDIT')")
    public User updateUser(@PathVariable String id, @RequestBody @Valid UpdateUserDto dto) throws UserNotFoundException {
        return userService.update(id, dto);
    }

    @DeleteMapping("api/v1/users/{id}")
    @PreAuthorize("hasPermission(null, 'USERS_DELETE')")
    public ResponseEntity<String> desactivateUser(@PathVariable String id) throws UserNotFoundException, UserAlreadyInactiveException {
        userService.desactivate(id);
        return  ResponseEntity.ok().build();
    }

}
