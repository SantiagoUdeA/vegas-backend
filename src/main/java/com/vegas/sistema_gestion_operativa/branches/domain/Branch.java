package com.vegas.sistema_gestion_operativa.branches.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Branch {

    private String id;
    private String name;
    private String address;
    private String phoneNumber;
}
