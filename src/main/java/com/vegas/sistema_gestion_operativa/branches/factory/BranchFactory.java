package com.vegas.sistema_gestion_operativa.branches.factory;

import com.vegas.sistema_gestion_operativa.branches.domain.Branch;
import com.vegas.sistema_gestion_operativa.branches.dto.CreateBranchDto;

public class BranchFactory {

    public static Branch createBranch(CreateBranchDto dto) {
        return Branch.builder()
                .name(dto.name())
                .address(dto.address())
                .phoneNumber(dto.phoneNumber())
                .build();
    }
}
