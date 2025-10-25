package com.vegas.sistema_gestion_operativa.raw_material.application.factory;

import com.vegas.sistema_gestion_operativa.raw_material.application.dto.CreateRawMaterialDto;
import com.vegas.sistema_gestion_operativa.raw_material.domain.entity.RawMaterial;
import com.vegas.sistema_gestion_operativa.raw_material.domain.entity.RawMaterialCategory;
import com.vegas.sistema_gestion_operativa.raw_material.domain.exceptions.RawMaterialCategoryNotFoundException;
import com.vegas.sistema_gestion_operativa.raw_material.infrastructure.repository.IRawMaterialCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RawMaterialFactory {

    private final IRawMaterialCategoryRepository categoryRepository;

    @Autowired
    public RawMaterialFactory(IRawMaterialCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public RawMaterial createFromDto(CreateRawMaterialDto dto) throws RawMaterialCategoryNotFoundException {
        RawMaterialCategory category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new RawMaterialCategoryNotFoundException(
                        "La categoría con id " + dto.categoryId() + " no fue encontrada"));

        return RawMaterial.builder()
                .name(dto.name())
                .unitOfMeasure(dto.unitOfMeasure())
                .category(category)
                .build();
    }
}

