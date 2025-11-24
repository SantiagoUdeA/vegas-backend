package com.vegas.sistema_gestion_operativa.products.application.service;

import com.vegas.sistema_gestion_operativa.branches.IBranchApi;
import com.vegas.sistema_gestion_operativa.common.domain.Quantity;
import com.vegas.sistema_gestion_operativa.common.exceptions.AccessDeniedException;
import com.vegas.sistema_gestion_operativa.products.api.IProductApi;
import com.vegas.sistema_gestion_operativa.products.api.IngredientDto;
import com.vegas.sistema_gestion_operativa.products.application.dto.CreateProductDto;
import com.vegas.sistema_gestion_operativa.products.api.ProductDto;
import com.vegas.sistema_gestion_operativa.products.application.dto.UpdateProductDto;
import com.vegas.sistema_gestion_operativa.products.application.factory.ProductFactory;
import com.vegas.sistema_gestion_operativa.products.application.mapper.IProductMapper;
import com.vegas.sistema_gestion_operativa.products.domain.entity.Product;
import com.vegas.sistema_gestion_operativa.products.domain.entity.ProductCategory;
import com.vegas.sistema_gestion_operativa.products.domain.exceptions.ProductCategoryNotFoundException;
import com.vegas.sistema_gestion_operativa.products.domain.exceptions.ProductNameAlreadyExists;
import com.vegas.sistema_gestion_operativa.products.domain.exceptions.ProductNotFoundException;
import com.vegas.sistema_gestion_operativa.products.domain.repository.IProductCategoryRepository;
import com.vegas.sistema_gestion_operativa.products.domain.repository.IProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class ProductService implements IProductApi {

    private final ProductFactory productFactory;
    private final IProductRepository productRepository;
    private final IProductCategoryRepository categoryRepository;
    private final IProductMapper productMapper;
    private final IBranchApi branchApi;

    @Autowired
    public ProductService(ProductFactory productFactory,
                          IProductRepository productRepository,
                          IProductCategoryRepository categoryRepository,
                          IProductMapper productMapper,
                          IBranchApi branchApi) {
        this.productFactory = productFactory;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
        this.branchApi = branchApi;
    }

    @Transactional(readOnly = true)
    public Page<ProductDto> findAll(
            Pageable pageable,
            String userId,
            Long branchId
    ) throws AccessDeniedException {
        branchApi.assertUserHasAccessToBranch(userId, branchId);
        Page<Product> products = this.productRepository.findByActiveTrueAndBranchId(branchId, pageable);
        return products.map(productMapper::toResponseDto);
    }

    public ProductDto create(
            CreateProductDto dto,
            String userId
    ) throws ProductCategoryNotFoundException, ProductNameAlreadyExists, AccessDeniedException {
        branchApi.assertUserHasAccessToBranch(userId, dto.branchId());

        if(this.productRepository.findByNameAndActiveTrueAndBranchId(dto.name(), dto.branchId()).isPresent()){
            throw new ProductNameAlreadyExists("El producto con nombre " + dto.name() + " ya existe en esta sede");
        }

        categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new ProductCategoryNotFoundException(
                        "La categoría con id " + dto.categoryId() + " no fue encontrada"));

        Product product = this.productRepository.save(this.productFactory.createFromDto(dto));
        return productMapper.toResponseDto(product);
    }

    public ProductDto update(
            Long productId,
            UpdateProductDto dto,
            String userId
    ) throws ProductNotFoundException, ProductCategoryNotFoundException, AccessDeniedException {

        var product = this.retrieveProductById(productId);

        branchApi.assertUserHasAccessToBranch(userId, product.getBranchId());

        ProductCategory category = product.getCategory();
        if (dto.categoryId() != null) {
            category = this.retrieveProductCategoryByIdOrThrow(dto.categoryId());
        }

        return productMapper.toResponseDto(
                this.productRepository.save(productMapper.partialUpdate(dto, category, product))
        );
    }

    @Transactional
    public ProductDto delete(Long productId, String userId)
            throws ProductNotFoundException, AccessDeniedException {

        var product = this.retrieveProductById(productId);

        branchApi.assertUserHasAccessToBranch(userId, product.getBranchId());

        Double stock = productRepository.findCurrentStockByProductId(productId);

        if (stock != null && stock > 0) {
            throw new IllegalStateException(
                    "No se puede eliminar el producto porque aún tiene stock (" + stock + ")."
            );
        }

        product.deactivate();
        productRepository.save(product);

        return productMapper.toResponseDto(product);
    }

    private Product retrieveProductById(Long id) throws ProductNotFoundException {
        return this.productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("El producto con id " + id + " no fue encontrado"));
    }

    private ProductCategory retrieveProductCategoryByIdOrThrow(Long id) throws ProductCategoryNotFoundException {
        return this.categoryRepository.findById(id)
                .orElseThrow(() -> new ProductCategoryNotFoundException("La categoría con id " + id + " no fue encontrada"));
    }

    @Override
    public Optional<List<IngredientDto>> getIngredientsForProductUnit(Long productId) throws ProductNotFoundException {
        var recipe = this.retrieveProductById(productId).getRecipe();
        if(recipe == null) {
            return Optional.empty();
        }
        return Optional.of(recipe.getIngredients().stream().map(ingredient -> IngredientDto.builder()
                .id(ingredient.getId())
                .rawMaterialId(ingredient.getRawMaterialId())
                .quantity(ingredient.getQuantity().divide(new Quantity(recipe.getUnitsProduced())))
                .build()).toList());
    }
}