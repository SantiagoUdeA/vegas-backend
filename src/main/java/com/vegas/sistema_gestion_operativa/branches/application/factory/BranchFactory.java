package com.vegas.sistema_gestion_operativa.branches.application.factory;

import com.vegas.sistema_gestion_operativa.branches.domain.entity.Branch;
import com.vegas.sistema_gestion_operativa.branches.application.dto.CreateBranchDto;

public class BranchFactory {

    private BranchFactory() {}

    public static Branch createBranch(CreateBranchDto dto) {
        return Branch.builder()
                .name(dto.name())
                .address(dto.address())
                .phoneNumber(dto.phoneNumber())
                .build();
    }
}
