package com.vegas.sistema_gestion_operativa.aws.controller;

import com.vegas.sistema_gestion_operativa.aws.service.CognitoIdentityService;
import com.vegas.sistema_gestion_operativa.aws.dto.CreateUserDto;
import com.vegas.sistema_gestion_operativa.aws.dto.ListUsersDto;
import com.vegas.sistema_gestion_operativa.aws.dto.ListUsersResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserResponse;

@RestController
public class CognitoIdentityController {

    private final CognitoIdentityService cognitoIdentityService;

    @Autowired
    public CognitoIdentityController(CognitoIdentityService cognitoIdentityService) {
        this.cognitoIdentityService = cognitoIdentityService;
    }

    @PostMapping("/api/v1/users")
    @PreAuthorize("hasRole('Administrador')")
    public ResponseEntity<AdminCreateUserResponse> createUser(CreateUserDto dto) {
        return ResponseEntity.ok(cognitoIdentityService.createUser(dto));
    }

    @GetMapping("/api/v1/users")
    @PreAuthorize("hasPermission(null, 'USERS_VIEW')")
    public ResponseEntity<ListUsersResponseDto> listUsers(
            @RequestParam(value = "pageSize") int pageSize,
            @RequestParam(value = "nextToken", required = false) String nextToken
    ) {
        return ResponseEntity.ok(cognitoIdentityService.listUsersPage(new ListUsersDto(pageSize, nextToken)));
    }
}