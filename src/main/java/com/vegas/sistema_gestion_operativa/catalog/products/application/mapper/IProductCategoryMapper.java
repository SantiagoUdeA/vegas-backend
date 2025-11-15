package com.vegas.sistema_gestion_operativa.catalog.products.application.mapper;

import com.vegas.sistema_gestion_operativa.catalog.products.application.dto.ProductCategoryResponseDto;
import com.vegas.sistema_gestion_operativa.catalog.products.application.dto.UpdateProductCategoryDto;
import com.vegas.sistema_gestion_operativa.catalog.products.domain.entity.ProductCategory;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface IProductCategoryMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProductCategory partialUpdate(UpdateProductCategoryDto dto, @MappingTarget ProductCategory category);

    ProductCategoryResponseDto toResponseDto(ProductCategory category);

    List<ProductCategoryResponseDto> toResponseDtoList(List<ProductCategory> categories);
}

