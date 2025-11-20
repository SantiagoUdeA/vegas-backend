package com.vegas.sistema_gestion_operativa.products.application.mapper;

import com.vegas.sistema_gestion_operativa.common.domain.Money;
import com.vegas.sistema_gestion_operativa.products.api.ProductDto;
import com.vegas.sistema_gestion_operativa.products.application.dto.UpdateProductDto;
import com.vegas.sistema_gestion_operativa.products.domain.entity.Ingredient;
import com.vegas.sistema_gestion_operativa.products.domain.entity.Product;
import com.vegas.sistema_gestion_operativa.products.domain.entity.ProductCategory;
import com.vegas.sistema_gestion_operativa.products.domain.entity.Recipe;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {IProductCategoryMapper.class})
public interface IProductMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "dto.name")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "price", source = "dto.price")
    Product partialUpdate(UpdateProductDto dto, ProductCategory category, @MappingTarget Product product);

    @Mapping(target = "price", source = "price.value")
    @Mapping(target = "recipe", source = "recipe")
    ProductDto toResponseDto(Product product);

    List<ProductDto> toResponseDtoList(List<Product> products);

    // Mapeo de Recipe a RecipeDto (para ProductDto)
    @Mapping(target = "productId", source = "product.id")
    com.vegas.sistema_gestion_operativa.products.api.RecipeDto toRecipeDto(Recipe recipe);

    // Mapeo de Ingredient a IngredientDto (para ProductDto)
    @Mapping(target = "recipeId", source = "recipe.id")
    @Mapping(target = "unitOfMeasure", expression = "java(ingredient.getRawMaterial() != null ? ingredient.getRawMaterial().getUnitOfMeasure().name() : null)")
    @Mapping(target = "rawMaterialName", source = "rawMaterial.name")
    @Mapping(target = "rawMaterialUnitOfMeasure", expression = "java(ingredient.getRawMaterial() != null ? ingredient.getRawMaterial().getUnitOfMeasure().name() : null)")
    com.vegas.sistema_gestion_operativa.products.api.IngredientDto toIngredientDto(Ingredient ingredient);

    default Money mapToMoney(BigDecimal price) {
        return price != null ? new Money(price) : null;
    }

    default BigDecimal mapToBigDecimal(Money money) {
        return money != null ? money.getValue() : null;
    }
}

