package com.vegas.sistema_gestion_operativa.raw_material_inventory.application.service;

import com.vegas.sistema_gestion_operativa.branches.IBranchApi;
import com.vegas.sistema_gestion_operativa.common.exceptions.AccessDeniedException;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.application.dto.RawMaterialMovementDto;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.repository.IRawMaterialMovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RawMaterialMovementService {

    private final IRawMaterialMovementRepository rawMaterialMovementRepository;
    private final IBranchApi branchApi;

    /**
     * Obtiene todos los movimientos de materias primas de una sede específica con paginación.
     * Verifica que el usuario tenga acceso a la sede antes de devolver los datos.
     *
     * @param pageable Parámetros de paginación (página, tamaño, orden)
     * @param userId   ID del usuario que solicita los movimientos
     * @param branchId ID de la sede
     * @return Página de movimientos de materias primas con información detallada
     * @throws AccessDeniedException si el usuario no tiene acceso a la sede
     */
    public Page<RawMaterialMovementDto> findAll(Pageable pageable, String userId, Long branchId) throws AccessDeniedException {
        branchApi.assertUserHasAccessToBranch(userId, branchId);
        return rawMaterialMovementRepository.findAllByBranchId(pageable, branchId);
    }
}

