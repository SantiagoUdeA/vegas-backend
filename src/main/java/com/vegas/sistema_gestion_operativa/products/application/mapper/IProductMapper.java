package com.vegas.sistema_gestion_operativa.products.application.mapper;

import com.vegas.sistema_gestion_operativa.common.domain.Money;
import com.vegas.sistema_gestion_operativa.products.api.IngredientDto;
import com.vegas.sistema_gestion_operativa.products.api.ProductDto;
import com.vegas.sistema_gestion_operativa.products.api.RecipeDto;
import com.vegas.sistema_gestion_operativa.products.application.dto.UpdateProductDto;
import com.vegas.sistema_gestion_operativa.products.domain.entity.Ingredient;
import com.vegas.sistema_gestion_operativa.products.domain.entity.Product;
import com.vegas.sistema_gestion_operativa.products.domain.entity.ProductCategory;
import com.vegas.sistema_gestion_operativa.products.domain.entity.Recipe;
import com.vegas.sistema_gestion_operativa.raw_material.domain.entity.RawMaterial;
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

    // Mapeo de Recipe a RecipeDto
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "ingredients", source = "ingredients")
    RecipeDto toRecipeDto(Recipe recipe);

    // Mapeo de Ingredient a IngredientDto
    @Mapping(target = "recipeId", source = "recipe.id")
    @Mapping(target = "rawMaterialId", source = "rawMaterial.id")
    @Mapping(target = "rawMaterialName", source = "rawMaterial.name")
    @Mapping(target = "rawMaterialUnitOfMeasure", source = "rawMaterial.unitOfMeasure")
    IngredientDto toIngredientDto(Ingredient ingredient);

    List<IngredientDto> toIngredientDtoList(List<Ingredient> ingredients);

    // Mapeo de IngredientDto a Ingredient
    @Mapping(target = "id", source = "ingredientDto.id")
    @Mapping(target = "quantity", source = "ingredientDto.quantity")
    @Mapping(target = "observations", source = "ingredientDto.observations")
    @Mapping(target = "rawMaterial", source = "rawMaterial")
    @Mapping(target = "recipe", ignore = true)
    Ingredient toIngredient(IngredientDto ingredientDto, RawMaterial rawMaterial);

    @AfterMapping
    default void linkIngredients(@MappingTarget Recipe recipe) {
        if (recipe.getIngredients() != null) {
            recipe.getIngredients().forEach(ingredient -> ingredient.setRecipe(recipe));
        }
    }

    default Money mapToMoney(BigDecimal price) {
        return price != null ? new Money(price) : null;
    }

    default BigDecimal mapToBigDecimal(Money money) {
        return money != null ? money.getValue() : null;
    }
}

