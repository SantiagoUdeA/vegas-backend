package com.vegas.sistema_gestion_operativa.reports.domain.entity;

import com.lowagie.text.pdf.PdfPTable;
import com.vegas.sistema_gestion_operativa.common.utils.DateTimeUtils;
import com.vegas.sistema_gestion_operativa.reports.domain.builder.IPdfBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class MovementReport {

    private final String branchName;
    private final String userName;
    private final String userRole;
    private final Long totalEntries;
    private final Long totalExits;
    private final Long totalMovements;
    private final Long totalAdjustments;
    private LocalDate fromDate;
    private LocalDate toDate;
    private List<MovementItem> items;

    /**
     * Genera el PDF del reporte de movimientos utilizando el builder proporcionado.
     *
     * @param pdfBuilder Implementación de IPdfBuilder a utilizar
     * @return Arreglo de bytes que representa el documento PDF
     */
    public byte[] generatePdf(IPdfBuilder pdfBuilder, String title) {
        pdfBuilder
                .addTitle(title)
                .addSubtitle("Resumen")
                .addLabelAndDescription("Sucursal", branchName)
                .addLabelAndDescription("Generado por", userName)
                .addLabelAndDescription("Cargo", userRole)
                .addLabelAndDescription("Fecha de generación", DateTimeUtils.formatDateTime(DateTimeUtils.nowInBogota()))
                .addLabelAndDescription("Período del reporte", DateTimeUtils.formatDate(fromDate.atStartOfDay()) +
                        " a " + DateTimeUtils.formatDate(toDate.atStartOfDay()))
                .addLabelAndDescription("Cantidad de movimientos", totalMovements.toString())
                .addLabelAndDescription("Cantidad de entradas", totalEntries.toString())
                .addLabelAndDescription("Cantidad de salidas", totalExits.toString())
                .addLabelAndDescription("Cantidad de ajustes", totalAdjustments.toString())
                .addSubtitle("Movimientos")
                .addTable(buildMovementsTable(pdfBuilder));

        return pdfBuilder.build();
    }

    /**
     * Construye la tabla de movimientos de inventario.
     *
     * @param pdfBuilder Builder para construir la tabla
     * @return Tabla PDF con los movimientos
     */
    private PdfPTable buildMovementsTable(IPdfBuilder pdfBuilder) {
        List<String> headers = List.of("Producto", "Categoria", "Tipo", "Cantidad", "Fecha");

        List<List<String>> rows = items.stream()
                .map(item -> List.of(
                        item.itemName(),
                        item.itemCategoryName(),
                        item.movementReason().toString(),
                        String.valueOf(item.quantity().getValue().intValue()),
                        DateTimeUtils.formatDateTime(item.movementDate())
                ))
                .toList();

        float[] widths = new float[]{3f, 3f, 3f, 2f, 3f};

        return pdfBuilder.buildTable(headers, rows, widths);
    }


}
