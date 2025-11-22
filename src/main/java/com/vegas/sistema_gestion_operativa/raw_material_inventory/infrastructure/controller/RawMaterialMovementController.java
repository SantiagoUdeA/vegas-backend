package com.vegas.sistema_gestion_operativa.raw_material_inventory.infrastructure.controller;

import com.vegas.sistema_gestion_operativa.common.dto.PageResponse;
import com.vegas.sistema_gestion_operativa.common.dto.PaginationRequest;
import com.vegas.sistema_gestion_operativa.common.exceptions.AccessDeniedException;
import com.vegas.sistema_gestion_operativa.common.utils.PaginationUtils;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.appliaction.dto.RawMaterialMovementDto;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.appliaction.service.RawMaterialMovementService;
import com.vegas.sistema_gestion_operativa.security.AuthUtils;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/v1/raw-material-movements")
public class RawMaterialMovementController {

    private final RawMaterialMovementService rawMaterialMovementService;

    @GetMapping
    public ResponseEntity<PageResponse<RawMaterialMovementDto>> findAll(
            PaginationRequest pageRequest,
            @NotNull(message = "El id de la sede no debe ser nulo") Long branchId
    ) throws AccessDeniedException {
        Pageable page = PaginationUtils.getPageable(pageRequest);
        Page<RawMaterialMovementDto> movements =
                rawMaterialMovementService.findAll(page, AuthUtils.getUserIdFromToken(), branchId);
        return ResponseEntity.ok(PageResponse.from(movements));
    }
}


