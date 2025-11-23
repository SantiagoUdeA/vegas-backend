package com.vegas.sistema_gestion_operativa.users.infrastructure.controller;

import com.vegas.sistema_gestion_operativa.common.dto.PageResponse;
import com.vegas.sistema_gestion_operativa.common.dto.PaginationRequest;
import com.vegas.sistema_gestion_operativa.common.exceptions.InvalidPropertyFilterException;
import com.vegas.sistema_gestion_operativa.common.utils.PaginationUtils;
import com.vegas.sistema_gestion_operativa.security.AuthUtils;
import com.vegas.sistema_gestion_operativa.users.domain.entity.User;
import com.vegas.sistema_gestion_operativa.users.application.dto.CreateUserDto;
import com.vegas.sistema_gestion_operativa.users.application.dto.UpdateUserDto;
import com.vegas.sistema_gestion_operativa.users.domain.exceptions.UserAlreadyActiveException;
import com.vegas.sistema_gestion_operativa.users.domain.exceptions.UserAlreadyExistsException;
import com.vegas.sistema_gestion_operativa.users.domain.exceptions.UserAlreadyInactiveException;
import com.vegas.sistema_gestion_operativa.users.domain.exceptions.UserNotFoundException;
import com.vegas.sistema_gestion_operativa.users.application.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<PageResponse<User>> findAll(PaginationRequest paginationRequest) throws InvalidPropertyFilterException {
        Pageable pageable = PaginationUtils.getPageable(paginationRequest);
        Page<User> userPage = userService.findAll(AuthUtils.getUserIdFromToken(), AuthUtils.getRoleNameFromToken(), pageable);
        return ResponseEntity.ok(PageResponse.from(userPage));
    }

    /**
     * Creates a new user.
     * Requires USERS_CREATE permission.
     * @param dto data to create the user
     * @return created user
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid CreateUserDto dto) throws UserAlreadyExistsException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.create(dto, AuthUtils.getRoleNameFromToken()));
    }

    /**
     * Updates an existing user.
     * @param id ID of the user to update
     * @param dto data to update the user
     * @return updated user
     */
    @PatchMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody @Valid UpdateUserDto dto) throws UserNotFoundException {
        return ResponseEntity.ok(userService.update(id, dto, AuthUtils.getRoleNameFromToken()));
    }

    /**
     * Deactivates a user by ID.
     * @param id ID of the user to deactivate
     * @return ResponseEntity with status OK
     * @throws UserNotFoundException if the user is not found
     * @throws UserAlreadyInactiveException if the user is already inactive
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> desactivateUser(@PathVariable String id) throws UserNotFoundException, UserAlreadyInactiveException {
        userService.desactivate(id, AuthUtils.getRoleNameFromToken());
        return  ResponseEntity.ok().build();
    }

    /**
     * Activates a user by ID.
     * @param id ID of the user to activate
     * @return ResponseEntity with status OK
     * @throws UserNotFoundException if the user is not found
     * @throws UserAlreadyActiveException if the user is already active
     */
    @PostMapping("/{id}/activate")
    public ResponseEntity<String> activateUser(@PathVariable String id) throws UserNotFoundException, UserAlreadyActiveException {
        userService.activateUser(id, AuthUtils.getRoleNameFromToken());
        return ResponseEntity.ok().build();
    }

}
