package com.vegas.sistema_gestion_operativa.aws.dto;

import java.time.Instant;
import java.util.List;

public record UserDto(
    String username,
    String email,
    String givenName,
    String familyName,
    String userStatus,
    boolean enabled,
    Instant userCreateDate,
    Instant userLastModifiedDate,
    List<String> groups
) {}
