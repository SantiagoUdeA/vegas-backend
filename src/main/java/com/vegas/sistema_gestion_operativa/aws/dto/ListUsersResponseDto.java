package com.vegas.sistema_gestion_operativa.aws.dto;

import java.util.List;

public record ListUsersResponseDto(
    List<UserDto> users,
    String paginationToken,
    boolean hasMoreUsers
) {}
