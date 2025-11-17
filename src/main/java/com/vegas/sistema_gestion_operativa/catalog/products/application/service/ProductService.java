package com.vegas.sistema_gestion_operativa.catalog.products.application.service;

import com.vegas.sistema_gestion_operativa.catalog.products.application.dto.CreateProductDto;
import com.vegas.sistema_gestion_operativa.catalog.products.api.ProductDto;
import com.vegas.sistema_gestion_operativa.catalog.products.application.dto.UpdateProductDto;
import com.vegas.sistema_gestion_operativa.catalog.products.application.factory.ProductFactory;
import com.vegas.sistema_gestion_operativa.catalog.products.application.mapper.IProductMapper;
import com.vegas.sistema_gestion_operativa.catalog.products.domain.entity.Product;
import com.vegas.sistema_gestion_operativa.catalog.products.domain.exceptions.ProductCategoryNotFoundException;
import com.vegas.sistema_gestion_operativa.catalog.products.domain.exceptions.ProductNameAlreadyExists;
import com.vegas.sistema_gestion_operativa.catalog.products.domain.exceptions.ProductNotFoundException;
import com.vegas.sistema_gestion_operativa.catalog.products.infrastructure.repository.IProductCategoryRepository;
import com.vegas.sistema_gestion_operativa.catalog.products.infrastructure.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
}

