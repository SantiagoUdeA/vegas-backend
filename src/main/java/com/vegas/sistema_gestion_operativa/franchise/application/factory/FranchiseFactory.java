package com.vegas.sistema_gestion_operativa.franchise.application.factory;

import com.vegas.sistema_gestion_operativa.franchise.application.dto.CreateFranchiseDto;
import com.vegas.sistema_gestion_operativa.franchise.domain.entity.Franchise;

public class FranchiseFactory {

    private FranchiseFactory() {
    }

    public static Franchise createFranchise(CreateFranchiseDto dto) {
        return Franchise.builder()
                .name(dto.name())
                .slug(dto.slug())
                .active(true)
                .build();
    }
}
