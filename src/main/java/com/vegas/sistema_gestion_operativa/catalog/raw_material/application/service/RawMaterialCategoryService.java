package com.vegas.sistema_gestion_operativa.catalog.raw_material.application.service;

import com.vegas.sistema_gestion_operativa.catalog.raw_material.application.dto.CreateRawMaterialCategoryDto;
import com.vegas.sistema_gestion_operativa.catalog.raw_material.application.dto.RawMaterialCategoryResponseDto;
import com.vegas.sistema_gestion_operativa.catalog.raw_material.application.dto.UpdateRawMaterialCategoryDto;
import com.vegas.sistema_gestion_operativa.catalog.raw_material.application.factory.RawMaterialCategoryFactory;
import com.vegas.sistema_gestion_operativa.catalog.raw_material.application.mapper.IRawMaterialCategoryMapper;
import com.vegas.sistema_gestion_operativa.catalog.raw_material.domain.entity.RawMaterialCategory;
import com.vegas.sistema_gestion_operativa.catalog.raw_material.domain.exceptions.RawMaterialCategoryNameAlreadyExistsException;
import com.vegas.sistema_gestion_operativa.catalog.raw_material.domain.exceptions.RawMaterialCategoryNotFoundException;
import com.vegas.sistema_gestion_operativa.catalog.raw_material.infrastructure.repository.IRawMaterialCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RawMaterialCategoryService {

    private final RawMaterialCategoryFactory categoryFactory;
    private final IRawMaterialCategoryRepository categoryRepository;
    private final IRawMaterialCategoryMapper categoryMapper;

    @Autowired
    public RawMaterialCategoryService(RawMaterialCategoryFactory categoryFactory,
                                      IRawMaterialCategoryRepository categoryRepository,
                                      IRawMaterialCategoryMapper categoryMapper) {
        this.categoryFactory = categoryFactory;
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public Page<RawMaterialCategoryResponseDto> findAll(Pageable pageable) {
        Page<RawMaterialCategory> categories = categoryRepository.findAll(pageable);
        return categories.map(categoryMapper::toResponseDto);
    }

    public RawMaterialCategoryResponseDto create(CreateRawMaterialCategoryDto dto) throws RawMaterialCategoryNameAlreadyExistsException {
        var category = this.categoryRepository.findByName(dto.name());
        if(category.isPresent())
            throw new RawMaterialCategoryNameAlreadyExistsException("Ya existe una categoría con el nombre: " + dto.name());

        RawMaterialCategory newCategory = this.categoryRepository.save(this.categoryFactory.createFromDto(dto));
        return categoryMapper.toResponseDto(newCategory);
    }

    public RawMaterialCategoryResponseDto update(Long categoryId, UpdateRawMaterialCategoryDto dto) throws RawMaterialCategoryNotFoundException {
        var category = this.retrieveCategoryById(categoryId);
        var updatedCategory = this.categoryMapper.partialUpdate(dto, category);
        RawMaterialCategory saved = this.categoryRepository.save(updatedCategory);
        return categoryMapper.toResponseDto(saved);
    }

    public RawMaterialCategoryResponseDto delete(Long categoryId) throws RawMaterialCategoryNotFoundException {
        var category = this.retrieveCategoryById(categoryId);
        categoryRepository.delete(category);
        return categoryMapper.toResponseDto(category);
    }

    private RawMaterialCategory retrieveCategoryById(Long id) throws RawMaterialCategoryNotFoundException {
        return this.categoryRepository.findById(id)
                .orElseThrow(() -> new RawMaterialCategoryNotFoundException(id));
    }
}

