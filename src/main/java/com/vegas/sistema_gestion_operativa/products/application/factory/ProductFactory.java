package com.vegas.sistema_gestion_operativa.products.application.factory;

import com.vegas.sistema_gestion_operativa.products.application.dto.CreateProductDto;
import com.vegas.sistema_gestion_operativa.products.domain.entity.Product;
import com.vegas.sistema_gestion_operativa.products.domain.entity.ProductCategory;
import org.springframework.stereotype.Component;

@Component
public class ProductFactory {

    public Product createFromDto(CreateProductDto dto) {

        var category = ProductCategory.builder().id(dto.categoryId()).build();

        return Product.builder()
                .name(dto.name())
                .unitOfMeasure(dto.unitOfMeasure())
                .category(category)
                .active(true)
                .build();
    }
}

