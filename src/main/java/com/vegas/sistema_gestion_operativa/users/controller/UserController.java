package com.vegas.sistema_gestion_operativa.users.controller;

import com.vegas.sistema_gestion_operativa.users.domain.User;
import com.vegas.sistema_gestion_operativa.users.dto.CreateUserDto;
import com.vegas.sistema_gestion_operativa.users.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("api/v1/users")
    @PreAuthorize("hasPermission(null, 'USERS_VIEW')")
    public List<User> findAll() {
        return userService.findAll();
    }

    @PostMapping("api/v1/users")
    @PreAuthorize("hasPermission(null, 'USERS_CREATE')" )
    public User createUser(CreateUserDto dto) {
        return userService.create(dto);
    }

}
