package com.vegas.sistema_gestion_operativa.aws.dto;

import lombok.Value;

@Value
public class CreateUserDto {

    String username;
    String given_name;
    String family_name;
    String email;
}
