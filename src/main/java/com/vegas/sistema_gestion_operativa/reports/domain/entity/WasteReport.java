package com.vegas.sistema_gestion_operativa.reports.domain.entity;

import com.lowagie.text.pdf.PdfPTable;
import com.vegas.sistema_gestion_operativa.common.domain.Money;
import com.vegas.sistema_gestion_operativa.common.utils.DateTimeUtils;
import com.vegas.sistema_gestion_operativa.reports.domain.builder.IPdfBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class WasteReport {
    private String branchName;
    private String userName;
    private String userRole;
    private LocalDate fromDate;
    private LocalDate toDate;
    private Money totalWastes;
    private Money totalAdjustments;
    private List<WasteItem> items;

    public byte[] generatePdf(IPdfBuilder builder, String title) {
        builder.init()
                .addTitle(title)
                .addSubtitle("Información General")
                .addLabelAndDescription("Sucursal", this.branchName)
                .addLabelAndDescription("Generado por", this.userName + " (" + this.userRole + ")")
                .addLabelAndDescription("Periodo", String.format("Del %s al %s",
                        DateTimeUtils.formatDate(this.fromDate.atStartOfDay()),
                        DateTimeUtils.formatDate(this.toDate.atStartOfDay())))
                .addLabelAndDescription("Fecha de generación", DateTimeUtils.formatDateTime(java.time.LocalDateTime.now()))
                .addSpace(1)
                .addSubtitle("Resumen")
                .addLabelAndDescription("Total Mermas", String.format("$ %,.2f", this.totalWastes.getValue()))
                .addLabelAndDescription("Total Ajustes", String.format("$ %,.2f", this.totalAdjustments.getValue()))
                .addSpace(2)
                .addSubtitle("Detalle de Mermas y Ajustes");

        List<String> headers = List.of(
                "Fecha", "Tipo Elemento", "Elemento", "Tipo Movimiento", "Cantidad", "Valor Total", "Motivo"
        );

        List<List<String>> rows = new ArrayList<>();
        for (WasteItem item : this.items) {
            String uom = item.unitOfMeasure() != null ? " " + item.unitOfMeasure().getSymbol() : "";
            rows.add(List.of(
                    DateTimeUtils.formatDateTime(item.movementDate()),
                    item.itemType(),
                    item.itemName(),
                    item.movementReason().name(),
                    String.format("%,.2f%s", item.quantity().getValue(), uom),
                    String.format("$ %,.2f", item.totalValue().getValue()),
                    item.justification() != null ? item.justification() : ""
            ));
        }

        PdfPTable table = builder.buildTable(headers, rows, new float[]{1.5f, 1.2f, 2f, 1.5f, 1f, 1.5f, 2f});
        builder.addTable(table);

        return builder.build();
    }
}
