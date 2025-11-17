package com.vegas.sistema_gestion_operativa.catalog.products.application.service;

import com.vegas.sistema_gestion_operativa.catalog.products.application.dto.CreateProductCategoryDto;
import com.vegas.sistema_gestion_operativa.catalog.products.api.ProductCategoryDto;
import com.vegas.sistema_gestion_operativa.catalog.products.application.dto.UpdateProductCategoryDto;
import com.vegas.sistema_gestion_operativa.catalog.products.application.factory.ProductCategoryFactory;
import com.vegas.sistema_gestion_operativa.catalog.products.application.mapper.IProductCategoryMapper;
import com.vegas.sistema_gestion_operativa.catalog.products.domain.entity.ProductCategory;
import com.vegas.sistema_gestion_operativa.catalog.products.domain.exceptions.ProductCategoryNameAlreadyExistsException;
import com.vegas.sistema_gestion_operativa.catalog.products.domain.exceptions.ProductCategoryNotFoundException;
import com.vegas.sistema_gestion_operativa.catalog.products.infrastructure.repository.IProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductCategoryService {

    private final ProductCategoryFactory categoryFactory;
    private final IProductCategoryRepository categoryRepository;
    private final IProductCategoryMapper categoryMapper;

    @Autowired
    public ProductCategoryService(ProductCategoryFactory categoryFactory,
                                  IProductCategoryRepository categoryRepository,
                                  IProductCategoryMapper categoryMapper) {
        this.categoryFactory = categoryFactory;
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public Page<ProductCategoryDto> findAll(Pageable pageable) {
        Page<ProductCategory> categories = categoryRepository.findAll(pageable);
        return categories.map(categoryMapper::toResponseDto);
    }

    public ProductCategoryDto create(CreateProductCategoryDto dto) throws ProductCategoryNameAlreadyExistsException {
        var category = this.categoryRepository.findByName(dto.name());
        if(category.isPresent())
            throw new ProductCategoryNameAlreadyExistsException("Ya existe una categoría con el nombre: " + dto.name());

        ProductCategory newCategory = this.categoryRepository.save(this.categoryFactory.createFromDto(dto));
        return categoryMapper.toResponseDto(newCategory);
    }

    public ProductCategoryDto update(Long categoryId, UpdateProductCategoryDto dto) throws ProductCategoryNotFoundException {
        var category = this.retrieveCategoryById(categoryId);
        var updatedCategory = this.categoryMapper.partialUpdate(dto, category);
        ProductCategory saved = this.categoryRepository.save(updatedCategory);
        return categoryMapper.toResponseDto(saved);
    }

    public ProductCategoryDto delete(Long categoryId) throws ProductCategoryNotFoundException {
        var category = this.retrieveCategoryById(categoryId);
        categoryRepository.delete(category);
        return categoryMapper.toResponseDto(category);
    }

    private ProductCategory retrieveCategoryById(Long id) throws ProductCategoryNotFoundException {
        return this.categoryRepository.findById(id)
                .orElseThrow(() -> new ProductCategoryNotFoundException(
                        "La categoría con id " + id + " no fue encontrada"));
    }
}

