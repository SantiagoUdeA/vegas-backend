package com.vegas.sistema_gestion_operativa.catalog.raw_material.application.factory;

import com.vegas.sistema_gestion_operativa.catalog.raw_material.application.dto.CreateRawMaterialDto;
import com.vegas.sistema_gestion_operativa.catalog.raw_material.domain.entity.RawMaterial;
import com.vegas.sistema_gestion_operativa.catalog.raw_material.domain.entity.RawMaterialCategory;
import com.vegas.sistema_gestion_operativa.common.domain.Money;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class RawMaterialFactory {

    public RawMaterial createFromDto(CreateRawMaterialDto dto) {

        var category = RawMaterialCategory.builder().id(dto.categoryId()).build();

        return RawMaterial.builder()
                .name(dto.name())
                .unitOfMeasure(dto.unitOfMeasure())
                .category(category)
                .branchId(dto.branchId())
                .active(true)
                .build();
    }
}

