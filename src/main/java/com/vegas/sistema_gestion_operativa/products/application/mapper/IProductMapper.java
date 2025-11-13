package com.vegas.sistema_gestion_operativa.products.application.mapper;

import com.vegas.sistema_gestion_operativa.products.application.dto.ProductResponseDto;
import com.vegas.sistema_gestion_operativa.products.application.dto.UpdateProductDto;
import com.vegas.sistema_gestion_operativa.products.domain.entity.Product;
import com.vegas.sistema_gestion_operativa.products.domain.entity.ProductCategory;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {IProductCategoryMapper.class})
public interface IProductMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "dto.name")
    @Mapping(target = "unitOfMeasure", source = "dto.unitOfMeasure")
    @Mapping(target = "category", source = "category")
    Product partialUpdate(UpdateProductDto dto, ProductCategory category, @MappingTarget Product product);

    ProductResponseDto toResponseDto(Product product);

    List<ProductResponseDto> toResponseDtoList(List<Product> products);
}

