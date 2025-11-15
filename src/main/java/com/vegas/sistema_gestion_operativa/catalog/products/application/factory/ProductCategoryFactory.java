package com.vegas.sistema_gestion_operativa.catalog.products.application.factory;

import com.vegas.sistema_gestion_operativa.catalog.products.application.dto.CreateProductCategoryDto;
import com.vegas.sistema_gestion_operativa.catalog.products.domain.entity.ProductCategory;
import org.springframework.stereotype.Component;

@Component
public class ProductCategoryFactory {

    public ProductCategory createFromDto(CreateProductCategoryDto dto) {
        return ProductCategory.builder()
                .name(dto.name())
                .description(dto.description())
                .active(true)
                .build();
    }
}

