package com.vegas.sistema_gestion_operativa.reports.application.service;

import com.lowagie.text.pdf.PdfPTable;
import com.vegas.sistema_gestion_operativa.branches.IBranchApi;
import com.vegas.sistema_gestion_operativa.common.exceptions.AccessDeniedException;
import com.vegas.sistema_gestion_operativa.common.utils.DateTimeUtils;
import com.vegas.sistema_gestion_operativa.products_inventory.api.ProductInventoryMovementDto;
import com.vegas.sistema_gestion_operativa.products_inventory.domain.exceptions.NoMovementsForReportGenerationException;
import com.vegas.sistema_gestion_operativa.reports.application.dto.GenerateMovementReportDto;
import com.vegas.sistema_gestion_operativa.reports.application.dto.ProductMovementsReportDto;
import com.vegas.sistema_gestion_operativa.reports.domain.repository.IReportsRepository;
import com.vegas.sistema_gestion_operativa.reports.utils.PdfBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryMovementsReportService {

    private final IReportsRepository reportsRepository;
    private final IBranchApi branchApi;

    /**
     * Constructor
     *
     * @param dto    Data transfer object containing report parameters
     * @param userId ID of the user requesting the report
     * @return Byte array representing the generated PDF report
     * @throws AccessDeniedException if the user does not have access to the specified branch
     */
    public byte[] generateReport(GenerateMovementReportDto dto, String userId) throws AccessDeniedException, NoMovementsForReportGenerationException {

        // Verify user access to the branch
        this.branchApi.assertUserHasAccessToBranch(userId, dto.branchId());

        // Fetch inventory movements based on the provided criteria
        List<ProductInventoryMovementDto> movements = reportsRepository.findMovementsForReport(
                dto.branchId(),
                dto.categoryId(),
                dto.fromDate(),
                dto.toDate()
        );

        if (movements.isEmpty())
            throw new NoMovementsForReportGenerationException("No se encontraron movimientos para los criterios proporcionados.");

        ProductMovementsReportDto report = reportsRepository.createMovementsReport(
                dto.branchId(),
                dto.categoryId(),
                dto.fromDate(),
                dto.toDate(),
                userId
        );

        // Build the PDF report
        PdfBuilder builder = new PdfBuilder()
                .addTitle("Reporte de Movimientos de Inventario de Productos")
                .addSubtitle("Resumen")
                .addLabelAndDescription("Sucursal", report.branchName())
                .addLabelAndDescription("Generado por", report.userName())
                .addLabelAndDescription("Cargo", report.userRole())
                .addLabelAndDescription("Fecha de generación", DateTimeUtils.formatDateTime(DateTimeUtils.nowInBogota()))
                .addLabelAndDescription("Período del reporte", DateTimeUtils.formatDate(dto.fromDate()) +
                        " a " + DateTimeUtils.formatDate(dto.toDate()))
                .addLabelAndDescription("Cantidad de movimientos", report.totalMovements().toString())
                .addLabelAndDescription("Cantidad de entradas", report.totalEntries().toString())
                .addLabelAndDescription("Cantidad de salidas", report.totalExits().toString())
                .addLabelAndDescription("Cantidad de ajustes", report.totalAdjustments().toString())
                .addSubtitle("Movimientos")
                .addTable(buildTable(movements));
        return builder.build();
    }

    /**
     * Construye la tabla de movimientos de inventario
     *
     * @param movements lista de movimientos de inventario
     * @return tabla PDF con los movimientos
     */
    private PdfPTable buildTable(List<ProductInventoryMovementDto> movements) {

        List<String> headers = List.of("Producto", "Categoria", "Tipo", "Cantidad", "Fecha");

        List<List<String>> rows = movements.stream()
                .map(m -> List.of(
                        m.productName(),
                        m.productCategoryName(),
                        m.movementReason().toString(),
                        String.valueOf(m.quantity().getValue().intValue()),
                        DateTimeUtils.formatDateTime(m.movementDate())
                ))
                .toList();

        // Dejar null si se quiere que las columnas tengan ancho uniforme
        float[] widths = new float[]{3f, 3f, 3f, 2f, 3f};

        return new PdfBuilder().buildTable(headers, rows, widths);
    }

}
