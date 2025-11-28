package com.vegas.sistema_gestion_operativa.sales.application.service;

import com.vegas.sistema_gestion_operativa.branches.IBranchApi;
import com.vegas.sistema_gestion_operativa.common.domain.Money;
import com.vegas.sistema_gestion_operativa.common.exceptions.ApiException;
import com.vegas.sistema_gestion_operativa.common.utils.DateTimeUtils;
import com.vegas.sistema_gestion_operativa.products.api.IProductApi;
import com.vegas.sistema_gestion_operativa.products.api.ProductDto;
import com.vegas.sistema_gestion_operativa.products_inventory.IProductInventoryApi;
import com.vegas.sistema_gestion_operativa.sales.application.dto.*;
import com.vegas.sistema_gestion_operativa.sales.domain.entity.Sale;
import com.vegas.sistema_gestion_operativa.sales.domain.entity.SaleDetail;
import com.vegas.sistema_gestion_operativa.sales.domain.repository.ISaleDetailRepository;
import com.vegas.sistema_gestion_operativa.sales.domain.repository.ISaleRepository;
import com.vegas.sistema_gestion_operativa.users.IUserApi;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final ISaleRepository saleRepository;
    private final ISaleDetailRepository saleDetailRepository;
    private final IProductInventoryApi productInventoryApi;
    private final IBranchApi branchApi;
    private final IUserApi userApi;
    private final IProductApi productApi;

    /**
     * Crea una nueva venta
     *
     * @param dto    datos de la venta
     * @param userId ID del usuario que crea la venta
     * @return la venta creada
     * @throws ApiException ocurre si el producto no es encontrado o hay stock insuficiente
     */
    @Transactional
    public Sale create(CreateSaleDto dto, String userId)
            throws ApiException {

        List<SaleDetail> details = new ArrayList<>();

        // Construir la venta
        Sale sale = Sale.builder()
                .saleDate(DateTimeUtils.nowInBogota())
                .branchId(dto.branchId())
                .employeeId(userId)
                .build();

        // Construir detalles de la venta con precios y subtotales
        for (CreateSaleDetailDto createSaleDetailDto : dto.details()) {

            ProductDto product = productApi.getProductByIdOrThrow(createSaleDetailDto.productId());

            SaleDetail saleDetail = SaleDetail.builder()
                    .productId(createSaleDetailDto.productId())
                    .quantity(createSaleDetailDto.quantity())
                    .unitPrice(product.getPrice())
                    .subtotal(product.getPrice().multiply(createSaleDetailDto.quantity()))
                    .sale(sale)
                    .build();

            details.add(saleDetail);
        }

        // Establecer total de la venta y detalles
        sale.setDetails(details);
        sale.setTotal(details.stream().reduce(
                new Money(0.0),
                (currentValue, detail) -> currentValue.add(detail.getSubtotal()), Money::add)
        );


        // Descontar inventario por cada producto vendido
        for (var detail : dto.details()) {
            productInventoryApi.consumeProductStock(
                    dto.branchId(),
                    detail.productId(),
                    detail.quantity(),
                    userId
            );
        }

        this.saleDetailRepository.saveAll(details);
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
            throws ApiException {

        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new ApiException("La venta no existe", HttpStatus.NOT_FOUND));

        // Verificar acceso a la sede donde ocurrió la venta
        branchApi.assertUserHasAccessToBranch(userId, sale.getBranchId());

        // Reponer stock por cada detalle
        for (var detail : sale.getDetails()) {
            productInventoryApi.restoreProductStock(
                    sale.getBranchId(),
                    detail.getProductId(),
                    detail.getQuantity(),
                    userId
            );
        }

        // Eliminar la venta
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
