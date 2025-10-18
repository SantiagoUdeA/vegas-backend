package com.vegas.sistema_gestion_operativa.users.infrastructure.controller;

import com.vegas.sistema_gestion_operativa.security.AuthUtils;
import com.vegas.sistema_gestion_operativa.users.domain.entity.User;
import com.vegas.sistema_gestion_operativa.users.application.dto.CreateUserDto;
import com.vegas.sistema_gestion_operativa.users.application.dto.UpdateUserDto;
import com.vegas.sistema_gestion_operativa.users.domain.exceptions.UserAlreadyExistsException;
import com.vegas.sistema_gestion_operativa.users.domain.exceptions.UserAlreadyInactiveException;
import com.vegas.sistema_gestion_operativa.users.domain.exceptions.UserNotFoundException;
import com.vegas.sistema_gestion_operativa.users.application.service.UserService;
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
@RequestMapping("/api/v1/users")
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
    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }

    /**
     * Creates a new user.
     * Requires USERS_CREATE permission.
     * @param dto data to create the user
     * @return created user
     */
    @PostMapping
    public User createUser(@RequestBody @Valid CreateUserDto dto) throws UserAlreadyExistsException {
        return userService.create(dto, AuthUtils.getUserIdFromToken(), AuthUtils.getRoleNameFromToken());
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'USERS_EDIT')")
    public User updateUser(@PathVariable String id, @RequestBody @Valid UpdateUserDto dto) throws UserNotFoundException {
        return userService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'USERS_DELETE')")
    public ResponseEntity<String> desactivateUser(@PathVariable String id) throws UserNotFoundException, UserAlreadyInactiveException {
        userService.desactivate(id);
        return  ResponseEntity.ok().build();
    }

}
