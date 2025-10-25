
package com.vegas.sistema_gestion_operativa.raw_material.application.mapper;

import com.vegas.sistema_gestion_operativa.raw_material.application.dto.RawMaterialCategoryResponseDto;
import com.vegas.sistema_gestion_operativa.raw_material.application.dto.UpdateRawMaterialCategoryDto;
import com.vegas.sistema_gestion_operativa.raw_material.domain.entity.RawMaterialCategory;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface IRawMaterialCategoryMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    RawMaterialCategory partialUpdate(UpdateRawMaterialCategoryDto dto, @MappingTarget RawMaterialCategory category);

    RawMaterialCategoryResponseDto toResponseDto(RawMaterialCategory category);

    List<RawMaterialCategoryResponseDto> toResponseDtoList(List<RawMaterialCategory> categories);
}
