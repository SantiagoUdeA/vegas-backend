package com.vegas.sistema_gestion_operativa.raw_material.application.service;

import com.vegas.sistema_gestion_operativa.raw_material.application.dto.CreateRawMaterialCategoryDto;
import com.vegas.sistema_gestion_operativa.raw_material.application.dto.RawMaterialCategoryResponseDto;
import com.vegas.sistema_gestion_operativa.raw_material.application.dto.UpdateRawMaterialCategoryDto;
import com.vegas.sistema_gestion_operativa.raw_material.application.factory.RawMaterialCategoryFactory;
import com.vegas.sistema_gestion_operativa.raw_material.application.mapper.IRawMaterialCategoryMapper;
import com.vegas.sistema_gestion_operativa.raw_material.domain.entity.RawMaterialCategory;
import com.vegas.sistema_gestion_operativa.raw_material.domain.exceptions.RawMaterialCategoryNotFoundException;
import com.vegas.sistema_gestion_operativa.raw_material.infrastructure.repository.IRawMaterialCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<RawMaterialCategoryResponseDto> findAll() {
        List<RawMaterialCategory> categories = categoryRepository.findAll();
        return categoryMapper.toResponseDtoList(categories);
    }

    public RawMaterialCategoryResponseDto create(CreateRawMaterialCategoryDto dto) {
        RawMaterialCategory category = this.categoryRepository.save(this.categoryFactory.createFromDto(dto));
        return categoryMapper.toResponseDto(category);
    }

    public RawMaterialCategoryResponseDto update(Long categoryId, UpdateRawMaterialCategoryDto dto) throws RawMaterialCategoryNotFoundException {
        var category = this.retrieveCategoryById(categoryId);
        var updatedCategory = this.categoryMapper.partialUpdate(dto, category);
        RawMaterialCategory saved = this.categoryRepository.save(updatedCategory);
        return categoryMapper.toResponseDto(saved);
    }

    public RawMaterialCategoryResponseDto delete(Long categoryId) throws RawMaterialCategoryNotFoundException {
        var category = this.retrieveCategoryById(categoryId);
        this.categoryRepository.delete(category);
        return categoryMapper.toResponseDto(category);
    }

    private RawMaterialCategory retrieveCategoryById(Long id) throws RawMaterialCategoryNotFoundException {
        return this.categoryRepository.findById(id)
                .orElseThrow(() -> new RawMaterialCategoryNotFoundException(
                        "La categoría con id " + id + " no fue encontrada"));
    }
}

