package com.vegas.sistema_gestion_operativa.raw_material.application.factory;

import com.vegas.sistema_gestion_operativa.raw_material.application.dto.CreateRawMaterialCategoryDto;
import com.vegas.sistema_gestion_operativa.raw_material.domain.entity.RawMaterialCategory;
import org.springframework.stereotype.Component;

@Component
public class RawMaterialCategoryFactory {

    public RawMaterialCategory createFromDto(CreateRawMaterialCategoryDto dto) {
        return RawMaterialCategory.builder()
                .name(dto.name())
                .description(dto.description())
                .build();
    }
}

