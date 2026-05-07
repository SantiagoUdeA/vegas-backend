package com.vegas.sistema_gestion_operativa.raw_material_inventory.application.service;

import com.vegas.sistema_gestion_operativa.branches.IBranchApi;
import com.vegas.sistema_gestion_operativa.common.domain.MovementReason;
import com.vegas.sistema_gestion_operativa.common.domain.Quantity;
import com.vegas.sistema_gestion_operativa.common.dto.PageResponse;
import com.vegas.sistema_gestion_operativa.common.exceptions.AccessDeniedException;
import com.vegas.sistema_gestion_operativa.production.domain.entity.Production;
import com.vegas.sistema_gestion_operativa.production.infrastructure.repository.ProductionRepositoryJpa;
import com.vegas.sistema_gestion_operativa.products.domain.repository.IProductRepository;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.application.dto.*;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.entity.RawMaterialMovement;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.exceptions.BatchNotFoundException;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.repository.IRawMateriaBatchRepository;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.repository.IRawMaterialMovementRepository;
import com.vegas.sistema_gestion_operativa.users.IUserApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * HU13 — Service for reading the full movement history of a raw-material batch.
 *
 * <p>Strategy: load ALL movements for the batch, compute running balance,
 * then paginate in memory. This is acceptable because a single batch rarely
 * has more than a few dozen movements; for large volumes the spec recommends
 * migrating to an SQL-offset approach (section 8 of the spec).</p>
 */
@Service
public class BatchHistoryService {

    /** MovementReasons that increase stock (entries). Everything else is a deduction. */
    private static final Set<MovementReason> ENTRY_REASONS = Set.of(
            MovementReason.ENTRADA,
            MovementReason.COMPRA,
            MovementReason.RETORNO,
            MovementReason.PRODUCT_ENTRY
    );

    private final IRawMateriaBatchRepository batchRepository;
    private final IRawMaterialMovementRepository movementRepository;
    private final ProductionRepositoryJpa productionRepository;
    private final IProductRepository productRepository;
    private final IUserApi userApi;
    private final IBranchApi branchApi;

    @Autowired
    public BatchHistoryService(
            IRawMateriaBatchRepository batchRepository,
            IRawMaterialMovementRepository movementRepository,
            ProductionRepositoryJpa productionRepository,
            IProductRepository productRepository,
            IUserApi userApi,
            IBranchApi branchApi
    ) {
        this.batchRepository = batchRepository;
        this.movementRepository = movementRepository;
        this.productionRepository = productionRepository;
        this.productRepository = productRepository;
        this.userApi = userApi;
        this.branchApi = branchApi;
    }

    // ── Public API ───────────────────────────────────────────────────────────

    /**
     * Returns the full movement history of a batch with running balances, paginated.
     *
     * @param batchId  batch identifier
     * @param pageable pagination params (page / size — sort is ignored, always date ASC)
     * @param userId   authenticated user id
     * @return {@link BatchHistoryResponseDto} with batch metadata + paginated movements
     * @throws BatchNotFoundException if the batch does not exist
     * @throws AccessDeniedException  if the user has no access to the batch's branch
     */
    public BatchHistoryResponseDto getBatchHistory(Long batchId, Pageable pageable, String userId)
            throws AccessDeniedException, BatchNotFoundException {

        // 1. Load batch metadata (404 if not found)
        BatchDetailDto batch = batchRepository.findBatchDetailById(batchId)
                .orElseThrow(() -> new BatchNotFoundException(batchId));

        // 2. Authorisation: user must have access to the batch's branch
        branchApi.assertUserHasAccessToBranch(userId, batch.branchId());

        // 3. Load ALL movements ordered chronologically
        List<RawMaterialMovement> allMovements = movementRepository.findAllByBatchIdOrdered(batchId);

        // 4. Compute running balance and build DTOs
        List<BatchMovementItemDto> allItems = buildMovementItems(allMovements);

        // 5. Paginate in memory
        int totalElements = allItems.size();
        int pageNum = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        int fromIndex = Math.min(pageNum * pageSize, totalElements);
        int toIndex = Math.min(fromIndex + pageSize, totalElements);
        List<BatchMovementItemDto> pageContent = allItems.subList(fromIndex, toIndex);

        int totalPages = (pageSize == 0) ? 1 : (int) Math.ceil((double) totalElements / pageSize);
        boolean isLast = toIndex >= totalElements;

        PageResponse<BatchMovementItemDto> movements = new PageResponse<>(
                pageContent,
                pageNum,
                pageSize,
                totalElements,
                totalPages,
                isLast
        );

        return new BatchHistoryResponseDto(batch, movements);
    }

    /**
     * Searches batches by batch-number substring (case-insensitive) within a branch.
     *
     * @param branchId active branch id
     * @param query    search text (partial batch number)
     * @param pageable pagination params
     * @param userId   authenticated user id
     * @return paginated list of matching batches
     * @throws AccessDeniedException if the user has no access to the branch
     */
    public PageResponse<BatchSearchResultDto> searchBatches(
            Long branchId,
            String query,
            Pageable pageable,
            String userId
    ) throws AccessDeniedException {

        // 1. Authorisation
        branchApi.assertUserHasAccessToBranch(userId, branchId);

        // 2. Execute query
        var page = batchRepository.searchByBatchNumber(branchId, query, pageable);

        return PageResponse.from(page);
    }

    // ── Private helpers ──────────────────────────────────────────────────────

    /**
     * Converts a list of movements (already sorted) into DTOs with running balance.
     */
    private List<BatchMovementItemDto> buildMovementItems(List<RawMaterialMovement> movements) {
        List<BatchMovementItemDto> items = new ArrayList<>(movements.size());
        BigDecimal runningBalance = BigDecimal.ZERO;

        for (RawMaterialMovement rmm : movements) {
            BigDecimal qty = rmm.getQuantity().getValue();

            if (ENTRY_REASONS.contains(rmm.getMovementReason())) {
                runningBalance = runningBalance.add(qty);
            } else {
                runningBalance = runningBalance.subtract(qty);
            }

            String userName = resolveUserName(rmm.getUserId());
            ProductionReferenceDto productionRef = resolveProductionReference(rmm.getProductionId());

            items.add(new BatchMovementItemDto(
                    rmm.getId(),
                    rmm.getMovementDate(),
                    rmm.getMovementReason(),
                    rmm.getQuantity(),
                    new Quantity(runningBalance),
                    userName,
                    rmm.getJustification(),
                    productionRef
            ));
        }

        return items;
    }

    /**
     * Resolves a userId to "GivenName FamilyName". Falls back to the raw id on error.
     */
    private String resolveUserName(String userId) {
        try {
            return userApi.getFullNameById(userId);
        } catch (Exception e) {
            return userId;
        }
    }

    /**
     * Resolves a productionId to a {@link ProductionReferenceDto}, or {@code null} if absent.
     */
    private ProductionReferenceDto resolveProductionReference(Long productionId) {
        if (productionId == null) {
            return null;
        }
        return productionRepository.findById(productionId)
                .map(this::toProductionReferenceDto)
                .orElse(null);
    }

    private ProductionReferenceDto toProductionReferenceDto(Production production) {
        String productName = productRepository.findById(production.getProductId())
                .map(p -> p.getName())
                .orElse("Producto desconocido");

        return new ProductionReferenceDto(
                production.getId(),
                productName,
                production.getQuantityProduced(),
                production.getProductionDate()
        );
    }
}
