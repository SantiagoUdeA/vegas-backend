package com.vegas.sistema_gestion_operativa.raw_material.application.mapper;

import com.vegas.sistema_gestion_operativa.raw_material.application.dto.RawMaterialResponseDto;
import com.vegas.sistema_gestion_operativa.raw_material.application.dto.UpdateRawMaterialDto;
import com.vegas.sistema_gestion_operativa.raw_material.domain.entity.RawMaterial;
import com.vegas.sistema_gestion_operativa.raw_material.domain.entity.RawMaterialCategory;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {IRawMaterialCategoryMapper.class})
public interface IRawMaterialMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "dto.name")
    @Mapping(target = "unitOfMeasure", source = "dto.unitOfMeasure")
    @Mapping(target = "category", source = "category")
    RawMaterial partialUpdate(UpdateRawMaterialDto dto, RawMaterialCategory category, @MappingTarget RawMaterial rawMaterial);

    RawMaterialResponseDto toResponseDto(RawMaterial rawMaterial);

    List<RawMaterialResponseDto> toResponseDtoList(Page<RawMaterial> rawMaterials);
}

