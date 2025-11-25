package com.vegas.sistema_gestion_operativa.sales.application.service;

import com.vegas.sistema_gestion_operativa.branches.IBranchApi;
import com.vegas.sistema_gestion_operativa.common.exceptions.AccessDeniedException;
import com.vegas.sistema_gestion_operativa.common.exceptions.ApiException;
import com.vegas.sistema_gestion_operativa.products.api.IProductApi;
import com.vegas.sistema_gestion_operativa.products_inventory.application.service.ProductInventoryService;
import com.vegas.sistema_gestion_operativa.sales.application.dto.CreateSaleDto;
import com.vegas.sistema_gestion_operativa.sales.application.dto.ProductSalesStatsDto;
import com.vegas.sistema_gestion_operativa.sales.application.dto.SaleFilterDto;
import com.vegas.sistema_gestion_operativa.sales.application.dto.SaleResponseDto;
import com.vegas.sistema_gestion_operativa.sales.application.factory.SaleFactory;
import com.vegas.sistema_gestion_operativa.sales.domain.entity.Sale;
import com.vegas.sistema_gestion_operativa.sales.domain.repository.ISaleRepository;
import com.vegas.sistema_gestion_operativa.security.AuthUtils;
import com.vegas.sistema_gestion_operativa.users.api.IUserApi;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final ISaleRepository saleRepository;
    private final SaleFactory saleFactory;
    private final ProductInventoryService productInventoryService;
    private final IBranchApi branchApi;
    private final IUserApi userApi;

    private final IProductApi productApi;

    @Transactional
    public Sale create(CreateSaleDto dto, String userId)
            throws AccessDeniedException, ApiException {

        Sale sale = saleFactory.createFromDto(dto);

        if (userId == null || userId.isBlank()) {
            userId = AuthUtils.getUserIdFromToken();
        }

        sale.setEmployeeId(userId);

        // 🔥 Descontar inventario por cada producto vendido
        for (var detail : dto.getDetails()) {
            productInventoryService.consumeProductStock(
                    dto.getBranchId(),
                    detail.getProductId(),
                    detail.getQuantity(),
                    userId
            );
        }

        return saleRepository.save(sale);
    }

    @Transactional(readOnly = true)
    public Page<SaleResponseDto> findAll(SaleFilterDto filters, Pageable pageable) {

        Page<Sale> page = saleRepository.findAllFiltered(
                filters.getBranchId(),
                filters.getFrom(),
                filters.getTo(),
                pageable
        );

        return page.map(this::mapToDto);
    }

    private SaleResponseDto mapToDto(Sale sale) {
        String employeeName = userApi.getFullNameById(sale.getEmployeeId());

        return SaleResponseDto.builder()
                .id(sale.getId())
                .saleDate(sale.getSaleDate())
                .total(sale.getTotal())
                .branchId(sale.getBranchId())
                .employeeId(sale.getEmployeeId())
                .employeeName(employeeName)
                .details(
                        sale.getDetails().stream().map(d -> {
                            String productName;

                            try {
                                productName = productApi.getProductNameById(d.getProductId());
                            } catch (Exception e) {
                                productName = "Producto no encontrado";
                            }

                            return SaleResponseDto.DetailDto.builder()
                                    .id(d.getId())
                                    .productId(d.getProductId())
                                    .productName(productName)
                                    .quantity(d.getQuantity())
                                    .unitPrice(d.getUnitPrice())
                                    .subtotal(d.getSubtotal())
                                    .build();
                        }).toList()
                )
                .build();
    }

    @Transactional
    public void deleteSale(Long saleId, String userId)
            throws AccessDeniedException, ApiException {

        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new ApiException("La venta no existe", HttpStatus.NOT_FOUND));

        // Verificar acceso a la sede donde ocurrió la venta
        branchApi.assertUserHasAccessToBranch(userId, sale.getBranchId());

        // 1️⃣ Reponer stock por cada detalle
        sale.getDetails().forEach(detail -> {
            try {
                productInventoryService.restoreProductStock(
                        sale.getBranchId(),
                        detail.getProductId(),
                        detail.getQuantity(),
                        userId
                );
            } catch (ApiException e) {
                // Re-lanzamos como unchecked para no romper el forEach
                throw new RuntimeException(e);
            }
        });

        // 2️⃣ Eliminar la venta
        saleRepository.delete(sale);
    }

    public Map<String, List<ProductSalesStatsDto>> getProductSalesStats(
            Long branchId, LocalDate from, LocalDate to) {

        List<ProductSalesStatsDto> top = saleRepository
                .findTopSellingProducts(branchId, from, to, 5);

        List<ProductSalesStatsDto> low = saleRepository
                .findLeastSellingProducts(branchId, from, to, 5);

        Map<String, List<ProductSalesStatsDto>> result = new HashMap<>();
        result.put("topSelling", top);
        result.put("leastSelling", low);

        return result;
    }

}
