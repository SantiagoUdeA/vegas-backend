package com.vegas.sistema_gestion_operativa.products.application.service;

import com.vegas.sistema_gestion_operativa.branches.IBranchApi;
import com.vegas.sistema_gestion_operativa.common.exceptions.AccessDeniedException;
import com.vegas.sistema_gestion_operativa.products.api.ProductCategoryDto;
import com.vegas.sistema_gestion_operativa.products.application.dto.CreateProductCategoryDto;
import com.vegas.sistema_gestion_operativa.products.application.dto.UpdateProductCategoryDto;
import com.vegas.sistema_gestion_operativa.products.application.factory.ProductCategoryFactory;
import com.vegas.sistema_gestion_operativa.products.application.mapper.IProductCategoryMapper;
import com.vegas.sistema_gestion_operativa.products.domain.entity.ProductCategory;
import com.vegas.sistema_gestion_operativa.products.domain.exceptions.ProductCategoryNameAlreadyExistsException;
import com.vegas.sistema_gestion_operativa.products.domain.exceptions.ProductCategoryNotFoundException;
import com.vegas.sistema_gestion_operativa.products.domain.repository.IProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductCategoryService {

    private final ProductCategoryFactory categoryFactory;
    private final IProductCategoryRepository categoryRepository;
    private final IProductCategoryMapper categoryMapper;
    private final IBranchApi branchApi;

    @Autowired
    public ProductCategoryService(ProductCategoryFactory categoryFactory,
                                  IProductCategoryRepository categoryRepository,
                                  IProductCategoryMapper categoryMapper,
                                  IBranchApi branchApi) {
        this.categoryFactory = categoryFactory;
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.branchApi = branchApi;
    }

    @Transactional(readOnly = true)
    public Page<ProductCategoryDto> findAll(Pageable pageable, String userId, Long branchId)
            throws AccessDeniedException {
        branchApi.assertUserHasAccessToBranch(userId, branchId);
        Page<ProductCategory> categories = categoryRepository.findAllByBranchId(branchId, pageable);
        return categories.map(categoryMapper::toResponseDto);
    }

    public ProductCategoryDto create(CreateProductCategoryDto dto, String userId)
            throws ProductCategoryNameAlreadyExistsException, AccessDeniedException {
        branchApi.assertUserHasAccessToBranch(userId, dto.branchId());

        var category = this.categoryRepository.findByNameAndBranchId(dto.name(), dto.branchId());
        if (category.isPresent())
            throw new ProductCategoryNameAlreadyExistsException("Ya existe una categoría con el nombre: " + dto.name());

        ProductCategory newCategory = this.categoryRepository.save(
                this.categoryFactory.createFromDto(dto, dto.branchId()));
        return categoryMapper.toResponseDto(newCategory);
    }

    public ProductCategoryDto update(Long categoryId, UpdateProductCategoryDto dto, String userId)
            throws ProductCategoryNotFoundException, AccessDeniedException {
        var category = this.retrieveCategoryById(categoryId);
        branchApi.assertUserHasAccessToBranch(userId, category.getBranchId());

        var updatedCategory = this.categoryMapper.partialUpdate(dto, category);
        ProductCategory saved = this.categoryRepository.save(updatedCategory);
        return categoryMapper.toResponseDto(saved);
    }

    public ProductCategoryDto delete(Long categoryId, String userId)
            throws ProductCategoryNotFoundException, AccessDeniedException {
        var category = this.retrieveCategoryById(categoryId);
        branchApi.assertUserHasAccessToBranch(userId, category.getBranchId());

        categoryRepository.delete(category);
        return categoryMapper.toResponseDto(category);
    }

    private ProductCategory retrieveCategoryById(Long id) throws ProductCategoryNotFoundException {
        return this.categoryRepository.findById(id)
                .orElseThrow(() -> new ProductCategoryNotFoundException(
                        "La categoría con id " + id + " no fue encontrada"));
    }
}
