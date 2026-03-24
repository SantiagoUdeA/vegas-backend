package com.vegas.sistema_gestion_operativa.raw_material.application.service;

import com.vegas.sistema_gestion_operativa.branches.IBranchApi;
import com.vegas.sistema_gestion_operativa.common.exceptions.AccessDeniedException;
import com.vegas.sistema_gestion_operativa.raw_material.application.dto.CreateRawMaterialCategoryDto;
import com.vegas.sistema_gestion_operativa.raw_material.application.dto.RawMaterialCategoryResponseDto;
import com.vegas.sistema_gestion_operativa.raw_material.application.dto.UpdateRawMaterialCategoryDto;
import com.vegas.sistema_gestion_operativa.raw_material.application.factory.RawMaterialCategoryFactory;
import com.vegas.sistema_gestion_operativa.raw_material.application.mapper.IRawMaterialCategoryMapper;
import com.vegas.sistema_gestion_operativa.raw_material.domain.entity.RawMaterialCategory;
import com.vegas.sistema_gestion_operativa.raw_material.domain.exceptions.RawMaterialCategoryNameAlreadyExistsException;
import com.vegas.sistema_gestion_operativa.raw_material.domain.exceptions.RawMaterialCategoryNotFoundException;
import com.vegas.sistema_gestion_operativa.raw_material.infrastructure.repository.IRawMaterialCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RawMaterialCategoryService {

    private final RawMaterialCategoryFactory categoryFactory;
    private final IRawMaterialCategoryRepository categoryRepository;
    private final IRawMaterialCategoryMapper categoryMapper;
    private final IBranchApi branchApi;

    @Autowired
    public RawMaterialCategoryService(RawMaterialCategoryFactory categoryFactory,
                                      IRawMaterialCategoryRepository categoryRepository,
                                      IRawMaterialCategoryMapper categoryMapper,
                                      IBranchApi branchApi) {
        this.categoryFactory = categoryFactory;
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.branchApi = branchApi;
    }

    @Transactional(readOnly = true)
    public Page<RawMaterialCategoryResponseDto> findAll(Pageable pageable, String userId, Long branchId)
            throws AccessDeniedException {
        branchApi.assertUserHasAccessToBranch(userId, branchId);
        Page<RawMaterialCategory> categories = categoryRepository.findAllByBranchId(branchId, pageable);
        return categories.map(categoryMapper::toResponseDto);
    }

    public RawMaterialCategoryResponseDto create(CreateRawMaterialCategoryDto dto, String userId)
            throws RawMaterialCategoryNameAlreadyExistsException, AccessDeniedException {
        branchApi.assertUserHasAccessToBranch(userId, dto.branchId());

        var category = this.categoryRepository.findByNameAndBranchId(dto.name(), dto.branchId());
        if (category.isPresent())
            throw new RawMaterialCategoryNameAlreadyExistsException("Ya existe una categoría con el nombre: " + dto.name());

        RawMaterialCategory newCategory = this.categoryRepository.save(
                this.categoryFactory.createFromDto(dto, dto.branchId()));
        return categoryMapper.toResponseDto(newCategory);
    }

    public RawMaterialCategoryResponseDto update(Long categoryId, UpdateRawMaterialCategoryDto dto, String userId)
            throws RawMaterialCategoryNotFoundException, AccessDeniedException {
        var category = this.retrieveCategoryById(categoryId);
        branchApi.assertUserHasAccessToBranch(userId, category.getBranchId());

        var updatedCategory = this.categoryMapper.partialUpdate(dto, category);
        RawMaterialCategory saved = this.categoryRepository.save(updatedCategory);
        return categoryMapper.toResponseDto(saved);
    }

    public RawMaterialCategoryResponseDto delete(Long categoryId, String userId)
            throws RawMaterialCategoryNotFoundException, AccessDeniedException {
        var category = this.retrieveCategoryById(categoryId);
        branchApi.assertUserHasAccessToBranch(userId, category.getBranchId());

        categoryRepository.delete(category);
        return categoryMapper.toResponseDto(category);
    }

    private RawMaterialCategory retrieveCategoryById(Long id) throws RawMaterialCategoryNotFoundException {
        return this.categoryRepository.findById(id)
                .orElseThrow(() -> new RawMaterialCategoryNotFoundException(id));
    }
}
