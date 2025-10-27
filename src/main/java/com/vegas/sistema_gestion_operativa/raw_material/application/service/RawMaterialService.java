package com.vegas.sistema_gestion_operativa.raw_material.application.service;

import com.vegas.sistema_gestion_operativa.raw_material.application.dto.CreateRawMaterialDto;
import com.vegas.sistema_gestion_operativa.raw_material.application.dto.RawMaterialResponseDto;
import com.vegas.sistema_gestion_operativa.raw_material.application.dto.UpdateRawMaterialDto;
import com.vegas.sistema_gestion_operativa.raw_material.application.factory.RawMaterialFactory;
import com.vegas.sistema_gestion_operativa.raw_material.application.mapper.IRawMaterialMapper;
import com.vegas.sistema_gestion_operativa.raw_material.domain.entity.RawMaterial;
import com.vegas.sistema_gestion_operativa.raw_material.domain.exceptions.RawMaterialCategoryNotFoundException;
import com.vegas.sistema_gestion_operativa.raw_material.domain.exceptions.RawMaterialNotFoundException;
import com.vegas.sistema_gestion_operativa.raw_material.infrastructure.repository.IRawMaterialCategoryRepository;
import com.vegas.sistema_gestion_operativa.raw_material.infrastructure.repository.IRawMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RawMaterialService {

    private final RawMaterialFactory rawMaterialFactory;
    private final IRawMaterialRepository rawMaterialRepository;
    private final IRawMaterialCategoryRepository categoryRepository;
    private final IRawMaterialMapper rawMaterialMapper;

    @Autowired
    public RawMaterialService(RawMaterialFactory rawMaterialFactory,
                              IRawMaterialRepository rawMaterialRepository,
                              IRawMaterialCategoryRepository categoryRepository,
                              IRawMaterialMapper rawMaterialMapper) {
        this.rawMaterialFactory = rawMaterialFactory;
        this.rawMaterialRepository = rawMaterialRepository;
        this.categoryRepository = categoryRepository;
        this.rawMaterialMapper = rawMaterialMapper;
    }

    public List<RawMaterialResponseDto> findAll() {
        List<RawMaterial> rawMaterials = rawMaterialRepository.findByActiveTrue();
        return rawMaterialMapper.toResponseDtoList(rawMaterials);
    }

    public RawMaterialResponseDto create(CreateRawMaterialDto dto) throws RawMaterialCategoryNotFoundException {
        RawMaterial rawMaterial = this.rawMaterialRepository.save(this.rawMaterialFactory.createFromDto(dto));
        return rawMaterialMapper.toResponseDto(rawMaterial);
    }

    public RawMaterialResponseDto update(Long rawMaterialId, UpdateRawMaterialDto dto) throws RawMaterialNotFoundException, RawMaterialCategoryNotFoundException {
        var rawMaterial = this.retrieveRawMaterialById(rawMaterialId);
        var category = this.categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new RawMaterialCategoryNotFoundException(
                        "La categoría con id " + dto.categoryId() + " no fue encontrada"));
        var updatedRawMaterial = this.rawMaterialMapper.partialUpdate(dto, category, rawMaterial);
        RawMaterial saved = this.rawMaterialRepository.save(updatedRawMaterial);
        return rawMaterialMapper.toResponseDto(saved);
    }

    public RawMaterialResponseDto delete(Long rawMaterialId) throws RawMaterialNotFoundException {
        var rawMaterial = this.retrieveRawMaterialById(rawMaterialId);
        rawMaterial.deactivate();
        this.rawMaterialRepository.save(rawMaterial);
        return rawMaterialMapper.toResponseDto(rawMaterial);
    }

    private RawMaterial retrieveRawMaterialById(Long id) throws RawMaterialNotFoundException {
        return this.rawMaterialRepository.findById(id)
                .orElseThrow(() -> new RawMaterialNotFoundException("La materia prima con id " + id + " no fue encontrada"));
    }
}
