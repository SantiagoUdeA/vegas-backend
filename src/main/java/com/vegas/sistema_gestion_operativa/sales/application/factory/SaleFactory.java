package com.vegas.sistema_gestion_operativa.sales.application.factory;

import com.vegas.sistema_gestion_operativa.products.domain.entity.Product;
import com.vegas.sistema_gestion_operativa.products.domain.repository.IProductRepository;
import com.vegas.sistema_gestion_operativa.sales.application.dto.CreateSaleDto;
import com.vegas.sistema_gestion_operativa.sales.domain.entity.Sale;
import com.vegas.sistema_gestion_operativa.sales.domain.entity.SaleDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SaleFactory {

    private final IProductRepository productRepository;

    public Sale createFromDto(CreateSaleDto dto) {

        Sale sale = Sale.builder()
                .saleDate(LocalDateTime.now())
                .branchId(dto.getBranchId())
                .build();

        // 1. Crear detalles sin total
        List<SaleDetail> details = dto.getDetails().stream().map(d -> {

            Product product = productRepository.findById(d.getProductId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + d.getProductId()));

            BigDecimal unitPrice = product.getPrice().getValue();
            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(d.getQuantity()));

            SaleDetail detail = SaleDetail.builder()
                    .productId(d.getProductId())
                    .quantity(d.getQuantity())
                    .unitPrice(unitPrice)
                    .subtotal(subtotal)
                    .sale(sale)
                    .build();

            return detail;

        }).toList();

        // 2. Sumar total fuera del stream
        BigDecimal total = details.stream()
                .map(SaleDetail::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        sale.setDetails(details);
        sale.setTotal(total);

        return sale;
    }
}
