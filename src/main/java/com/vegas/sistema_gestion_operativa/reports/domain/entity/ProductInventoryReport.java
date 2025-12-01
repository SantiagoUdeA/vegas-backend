package com.vegas.sistema_gestion_operativa.reports.domain.entity;

import com.lowagie.text.pdf.PdfPTable;
import com.vegas.sistema_gestion_operativa.common.utils.DateTimeUtils;
import com.vegas.sistema_gestion_operativa.reports.domain.builder.IPdfBuilder;
import lombok.Getter;

import java.util.List;

@Getter
public class ProductInventoryReport extends Report {

    private List<ProductInventoryItem> productInventoryItems;

    public ProductInventoryReport(String branchName, String userName, String userRole) {
        super(branchName, userName, userRole);
    }

    public ProductInventoryReport(String branchName, String userName, String userRole, List<ProductInventoryItem> productInventoryItems) {
        super(branchName, userName, userRole);
        this.productInventoryItems = productInventoryItems;
    }

    @Override
    public byte[] generatePdf(IPdfBuilder pdfBuilder, String title) {
        pdfBuilder
                .init()
                .addTitle(title)
                .addSubtitle("Resumen")
                .addLabelAndDescription("Sucursal", this.branchName)
                .addLabelAndDescription("Generado por", this.userName)
                .addLabelAndDescription("Cargo", this.userRole)
                .addLabelAndDescription("Fecha de generación", DateTimeUtils.formatDateTime(DateTimeUtils.nowInBogota()))
                .addSubtitle("Inventario actual")
                .addTable(buildMovementsTable(pdfBuilder));

        return pdfBuilder.build();
    }

    private PdfPTable buildMovementsTable(IPdfBuilder pdfBuilder) {
        List<String> headers = List.of("Item", "Categoria", "Stock Actual", "Costo unitario", "Ultima Actualización");

        List<List<String>> rows = productInventoryItems.stream()
                .map(item -> List.of(
                        item.itemName(),
                        item.itemCategoryName(),
                        String.valueOf(item.currentStock().getValue().intValue()),
                        item.unitCost().formatToCurrencyString(),
                        DateTimeUtils.formatDateTime(item.updatedAt()))
                )
                .toList();

        float[] widths = new float[]{3f, 3f, 3f, 3f, 4f};

        return pdfBuilder.buildTable(headers, rows, widths);
    }
}
