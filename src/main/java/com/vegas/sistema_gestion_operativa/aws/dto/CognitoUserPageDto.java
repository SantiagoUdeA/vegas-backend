package com.vegas.sistema_gestion_operativa.aws.dto;

import software.amazon.awssdk.services.cognitoidentityprovider.model.UserType;

import java.util.List;

public record CognitoUserPageDto(List<UserType> users, String nextToken, int pageSize) {
}
