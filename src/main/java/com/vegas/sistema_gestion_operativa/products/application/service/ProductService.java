package com.vegas.sistema_gestion_operativa.products.application.service;

import com.vegas.sistema_gestion_operativa.production.application.dto.ProductWithRecipeDto;
import com.vegas.sistema_gestion_operativa.production.domain.entity.Ingredient;
import com.vegas.sistema_gestion_operativa.production.domain.entity.Recipe;
import com.vegas.sistema_gestion_operativa.products.application.dto.CreateProductDto;
import com.vegas.sistema_gestion_operativa.products.api.ProductDto;
import com.vegas.sistema_gestion_operativa.products.application.dto.UpdateProductDto;
import com.vegas.sistema_gestion_operativa.products.application.factory.ProductFactory;
import com.vegas.sistema_gestion_operativa.products.application.mapper.IProductMapper;
import com.vegas.sistema_gestion_operativa.products.domain.entity.Product;
import com.vegas.sistema_gestion_operativa.products.domain.exceptions.ProductCategoryNotFoundException;
import com.vegas.sistema_gestion_operativa.products.domain.exceptions.ProductNameAlreadyExists;
import com.vegas.sistema_gestion_operativa.products.domain.exceptions.ProductNotFoundException;
import com.vegas.sistema_gestion_operativa.products.infrastructure.repository.IProductCategoryRepository;
import com.vegas.sistema_gestion_operativa.products.infrastructure.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductFactory productFactory;
    private final IProductRepository productRepository;
    private final IProductCategoryRepository categoryRepository;
    private final IProductMapper productMapper;

    @Autowired
    public ProductService(ProductFactory productFactory,
                          IProductRepository productRepository,
                          IProductCategoryRepository categoryRepository,
                          IProductMapper productMapper) {
        this.productFactory = productFactory;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }

    public Page<ProductDto> findAll(Pageable pageable) {
        Page<Product> products = productRepository.findByActiveTrue(pageable);
        return products.map(productMapper::toResponseDto);
    }

    @Transactional(readOnly = true)
    public Page<ProductWithRecipeDto> getProductsWithRecipes(Pageable pageable) {
        Page<Product> products = this.productRepository.findAllProductsWithRecipes(pageable);
        return products.map(this::mapToDto);
    }

    public ProductDto create(CreateProductDto dto) throws ProductCategoryNotFoundException, ProductNameAlreadyExists {

        if(this.productRepository.findByNameAndActiveTrue(dto.name()).isPresent()){
            throw new ProductNameAlreadyExists("El producto con nombre " + dto.name() + " ya existe");
        }

        categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new ProductCategoryNotFoundException(
                        "La categoría con id " + dto.categoryId() + " no fue encontrada"));

        Product product = this.productRepository.save(this.productFactory.createFromDto(dto));
        return productMapper.toResponseDto(product);
    }

    public ProductDto update(Long productId, UpdateProductDto dto) throws ProductNotFoundException, ProductCategoryNotFoundException {
        var product = this.retrieveProductById(productId);
        var category = this.categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new ProductCategoryNotFoundException(
                        "La categoría con id " + dto.categoryId() + " no fue encontrada"));
        var updatedProduct = this.productMapper.partialUpdate(dto, category, product);
        updatedProduct.setActive(true);
        Product saved = this.productRepository.save(updatedProduct);
        return productMapper.toResponseDto(saved);
    }

    public ProductDto delete(Long productId) throws ProductNotFoundException {
        var product = this.retrieveProductById(productId);
        product.deactivate();
        this.productRepository.save(product);
        return productMapper.toResponseDto(product);
    }

    private Product retrieveProductById(Long id) throws ProductNotFoundException {
        return this.productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("El producto con id " + id + " no fue encontrado"));
    }

    private ProductWithRecipeDto mapToDto(Product product) {
        ProductWithRecipeDto dto = new ProductWithRecipeDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setActive(product.isActive());

        Recipe recipe = product.getRecipe();
        if (recipe != null) {
            ProductWithRecipeDto.RecipeDto recipeDto = new ProductWithRecipeDto.RecipeDto();
            recipeDto.setId(recipe.getId());
            recipeDto.setUnitsProduced(recipe.getUnitsProduced());
            recipeDto.setActive(recipe.getActive());
            recipeDto.setObservations(recipe.getObservations());
            recipeDto.setProductId(recipe.getProductId());

            List<ProductWithRecipeDto.IngredientDto> ingredientDtos =
                    recipe.getIngredients().stream()
                            .map(this::mapIngredient)
                            .toList();

            recipeDto.setIngredients(ingredientDtos);
            dto.setRecipe(recipeDto);
        }

        return dto;
    }

    private ProductWithRecipeDto.IngredientDto mapIngredient(Ingredient ingredient) {
        ProductWithRecipeDto.IngredientDto dto = new ProductWithRecipeDto.IngredientDto();
        dto.setId(ingredient.getId());
        dto.setObservations(ingredient.getObservations());
        dto.setRawMaterialId(ingredient.getRawMaterialId());
        dto.setQuantity(ingredient.getQuantity());

        if (ingredient.getRawMaterial() != null) {
            ProductWithRecipeDto.RawMaterialDto rawMaterialDto =
                    new ProductWithRecipeDto.RawMaterialDto();
            rawMaterialDto.setName(ingredient.getRawMaterial().getName());
            rawMaterialDto.setUnitOfMeasure(ingredient.getRawMaterial().getUnitOfMeasure().name());
            dto.setRawMaterial(rawMaterialDto);
        }

        return dto;
    }
}
