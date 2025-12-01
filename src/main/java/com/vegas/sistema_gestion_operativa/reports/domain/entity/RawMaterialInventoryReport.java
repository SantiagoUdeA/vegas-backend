package com.vegas.sistema_gestion_operativa.reports.domain.entity;

import com.lowagie.text.pdf.PdfPTable;
import com.vegas.sistema_gestion_operativa.common.utils.DateTimeUtils;
import com.vegas.sistema_gestion_operativa.reports.domain.builder.IPdfBuilder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class RawMaterialInventoryReport extends Report {

    private List<RawMaterialInventoryItem> inventoryItems;

    public RawMaterialInventoryReport(String branchName, String userName, String userRole) {
        super(branchName, userName, userRole);
    }

    public RawMaterialInventoryReport(String branchName, String userName, String userRole, List<RawMaterialInventoryItem> inventoryItems) {
        super(branchName, userName, userRole);
        this.inventoryItems = inventoryItems;
    }

    @Override
    public byte[] generatePdf(IPdfBuilder pdfBuilder, String title) {
        pdfBuilder
                .init()
                .addTitle(title)
                .addSubtitle("Resumen")
                .addLabelAndDescription("Sucursal", branchName)
                .addLabelAndDescription("Generado por", userName)
                .addLabelAndDescription("Cargo", userRole)
                .addLabelAndDescription("Fecha de generación", DateTimeUtils.formatDateTime(DateTimeUtils.nowInBogota()))
                .addLabelAndDescription("Total de materias primas", String.valueOf(inventoryItems.size()))
                .addSubtitle("Inventario de Materias Primas")
                .addTable(buildInventoryTable(pdfBuilder));

        return pdfBuilder.build();
    }

    /**
     * Construye la tabla de inventario de materias primas.
     *
     * @param pdfBuilder Builder para construir la tabla
     * @return Tabla PDF con el inventario
     */
    private PdfPTable buildInventoryTable(IPdfBuilder pdfBuilder) {
        List<String> headers = List.of("Materia Prima", "Categoría", "Stock", "Costo Promedio", "Última Actualización");

        List<List<String>> rows = inventoryItems.stream()
                .map(item -> {
                    BigDecimal stockValue = item.currentStock().getValue();
                    String quantityString = stockValue.stripTrailingZeros().toPlainString();
                    String formattedQuantity = quantityString +
                            (item.unitOfMeasure() != null ? " " + item.unitOfMeasure().getSymbol() : "");

                    BigDecimal costValue = item.averageCost().getValue();
                    String formattedCost = "$" + costValue.stripTrailingZeros().toPlainString();

                    return List.of(
                            item.itemName(),
                            item.itemCategoryName(),
                            formattedQuantity,
                            formattedCost,
                            DateTimeUtils.formatDateTime(item.updatedAt())
                    );
                })
                .toList();

        float[] widths = new float[]{3f, 2.5f, 2f, 2.5f, 3f};

        return pdfBuilder.buildTable(headers, rows, widths);
    }
}

