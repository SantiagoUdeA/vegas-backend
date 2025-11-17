package com.vegas.sistema_gestion_operativa.catalog.products.application.mapper;

import com.vegas.sistema_gestion_operativa.catalog.products.api.ProductDto;
import com.vegas.sistema_gestion_operativa.catalog.products.application.dto.UpdateProductDto;
import com.vegas.sistema_gestion_operativa.catalog.products.domain.entity.Product;
import com.vegas.sistema_gestion_operativa.catalog.products.domain.entity.ProductCategory;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {IProductCategoryMapper.class})
public interface IProductMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "dto.name")
    @Mapping(target = "category", source = "category")
    Product partialUpdate(UpdateProductDto dto, ProductCategory category, @MappingTarget Product product);

    ProductDto toResponseDto(Product product);

    List<ProductDto> toResponseDtoList(List<Product> products);
}

