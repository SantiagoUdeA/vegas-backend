package com.vegas.sistema_gestion_operativa.raw_material_inventory.application.service;

import com.vegas.sistema_gestion_operativa.branches.IBranchApi;
import com.vegas.sistema_gestion_operativa.common.exceptions.AccessDeniedException;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.application.dto.RawMaterialValuationItemDto;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.application.dto.RawMaterialValuationResponseDto;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.repository.IRawMaterialInventoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RawMaterialValuationService {

    private final IRawMaterialInventoryRepository repo;
    private final IBranchApi branchApi;

    public RawMaterialValuationService(IRawMaterialInventoryRepository repo, IBranchApi branchApi) {
        this.repo = repo;
        this.branchApi = branchApi;
    }

    public RawMaterialValuationResponseDto calculateValuation(Long branchId, String userId) throws AccessDeniedException {

        branchApi.assertUserHasAccessToBranch(userId, branchId);

        List<RawMaterialValuationItemDto> items = repo.findValuationItemsByBranchId(branchId);

        Double total = items.stream()
                .mapToDouble(RawMaterialValuationItemDto::getValuation)
                .sum();

        Map<String, Double> valuationByCategory = items.stream()
                .collect(Collectors.groupingBy(
                        RawMaterialValuationItemDto::getCategoryName,
                        Collectors.summingDouble(RawMaterialValuationItemDto::getValuation)
                ));

        return new RawMaterialValuationResponseDto(total, valuationByCategory, items);
    }
}