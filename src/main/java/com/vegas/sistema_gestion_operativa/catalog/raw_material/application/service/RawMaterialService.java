package com.vegas.sistema_gestion_operativa.catalog.raw_material.application.service;

import com.vegas.sistema_gestion_operativa.branches.IBranchApi;
import com.vegas.sistema_gestion_operativa.catalog.raw_material.application.dto.CreateRawMaterialDto;
import com.vegas.sistema_gestion_operativa.catalog.raw_material.application.dto.RawMaterialResponseDto;
import com.vegas.sistema_gestion_operativa.catalog.raw_material.application.dto.UpdateRawMaterialDto;
import com.vegas.sistema_gestion_operativa.catalog.raw_material.application.factory.RawMaterialFactory;
import com.vegas.sistema_gestion_operativa.catalog.raw_material.application.mapper.IRawMaterialMapper;
import com.vegas.sistema_gestion_operativa.catalog.raw_material.domain.entity.RawMaterial;
import com.vegas.sistema_gestion_operativa.catalog.raw_material.domain.exceptions.RawMaterialCategoryNotFoundException;
import com.vegas.sistema_gestion_operativa.catalog.raw_material.domain.exceptions.RawMaterialNameAlreadyExists;
import com.vegas.sistema_gestion_operativa.catalog.raw_material.domain.exceptions.RawMaterialNotFoundException;
import com.vegas.sistema_gestion_operativa.catalog.raw_material.infrastructure.repository.IRawMaterialCategoryRepository;
import com.vegas.sistema_gestion_operativa.catalog.raw_material.infrastructure.repository.IRawMaterialRepository;
import com.vegas.sistema_gestion_operativa.common.exceptions.AccessDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RawMaterialService {

    private final RawMaterialFactory rawMaterialFactory;
    private final IRawMaterialRepository rawMaterialRepository;
    private final IRawMaterialCategoryRepository categoryRepository;
    private final IRawMaterialMapper rawMaterialMapper;
    private final IBranchApi branchApi;

    @Autowired
    public RawMaterialService(RawMaterialFactory rawMaterialFactory,
                              IRawMaterialRepository rawMaterialRepository,
                              IRawMaterialCategoryRepository categoryRepository,
                              IRawMaterialMapper rawMaterialMapper, IBranchApi branchApi) {
        this.rawMaterialFactory = rawMaterialFactory;
        this.rawMaterialRepository = rawMaterialRepository;
        this.categoryRepository = categoryRepository;
        this.rawMaterialMapper = rawMaterialMapper;
        this.branchApi = branchApi;
    }

    public Page<RawMaterialResponseDto> findAll(
            Pageable pageable,
            String userId,
            Long branchId
    ) throws AccessDeniedException {
        branchApi.assertUserHasAccessToBranch(userId, branchId);
        Page<RawMaterial> rawMaterials = rawMaterialRepository.findByActiveTrueAndBranchId(branchId, pageable);
        return rawMaterials.map(rawMaterialMapper::toResponseDto);
    }

    public RawMaterialResponseDto create(
            CreateRawMaterialDto dto,
            String userId
    ) throws RawMaterialCategoryNotFoundException, RawMaterialNameAlreadyExists, AccessDeniedException {
        branchApi.assertUserHasAccessToBranch(userId, dto.branchId());
        if (this.rawMaterialRepository.findByNameAndActiveTrueAndBranchId(dto.name(), dto.branchId()).isPresent()) {
            throw new RawMaterialNameAlreadyExists("La materia prima con nombre " + dto.name() + " ya existe");
        }

        categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new RawMaterialCategoryNotFoundException(
                        "La categoría con id " + dto.categoryId() + " no fue encontrada"));

        RawMaterial rawMaterial = this.rawMaterialRepository.save(this.rawMaterialFactory.createFromDto(dto));
        return rawMaterialMapper.toResponseDto(rawMaterial);
    }

    public RawMaterialResponseDto update(
            Long rawMaterialId,
            UpdateRawMaterialDto dto,
            String userId
    ) throws RawMaterialNotFoundException, RawMaterialCategoryNotFoundException, AccessDeniedException {
        var rawMaterial = this.retrieveRawMaterialById(rawMaterialId);

        branchApi.assertUserHasAccessToBranch(userId, rawMaterial.getBranchId());

        var category = this.categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new RawMaterialCategoryNotFoundException(
                        "La categoría con id " + dto.categoryId() + " no fue encontrada"));

        var updatedRawMaterial = this.rawMaterialMapper.partialUpdate(dto, category, rawMaterial);
        updatedRawMaterial.setActive(true);
        RawMaterial saved = this.rawMaterialRepository.save(updatedRawMaterial);
        return rawMaterialMapper.toResponseDto(saved);
    }

    public RawMaterialResponseDto delete(
            Long rawMaterialId,
            String userId
    ) throws RawMaterialNotFoundException, AccessDeniedException {
        var rawMaterial = this.retrieveRawMaterialById(rawMaterialId);

        branchApi.assertUserHasAccessToBranch(userId, rawMaterial.getBranchId());

        rawMaterial.deactivate();
        this.rawMaterialRepository.save(rawMaterial);
        return rawMaterialMapper.toResponseDto(rawMaterial);
    }

    private RawMaterial retrieveRawMaterialById(Long id) throws RawMaterialNotFoundException {
        return this.rawMaterialRepository.findById(id)
                .orElseThrow(() -> new RawMaterialNotFoundException("La materia prima con id " + id + " no fue encontrada"));
    }
}
