package com.vegas.sistema_gestion_operativa.reports.application.service;

import com.vegas.sistema_gestion_operativa.branches.IBranchApi;
import com.vegas.sistema_gestion_operativa.common.exceptions.AccessDeniedException;
import com.vegas.sistema_gestion_operativa.products_inventory.domain.exceptions.NoMovementsForReportGenerationException;
import com.vegas.sistema_gestion_operativa.reports.application.dto.GenerateMovementReportDto;
import com.vegas.sistema_gestion_operativa.reports.domain.entity.MovementReport;
import com.vegas.sistema_gestion_operativa.reports.domain.repository.IReportsRepository;
import com.vegas.sistema_gestion_operativa.reports.infrastructure.builder.PdfBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryMovementsReportService {

    private final IReportsRepository reportsRepository;
    private final IBranchApi branchApi;
    private final ObjectProvider<PdfBuilder> pdfBuilder;

    /**
     * Genera el reporte de movimientos de inventario en formato PDF.
     *
     * @param dto    Data transfer object containing report parameters
     * @param userId ID of the user requesting the report
     * @return Byte array representing the generated PDF report
     * @throws AccessDeniedException                   if the user does not have access to the specified branch
     * @throws NoMovementsForReportGenerationException if no movements are found for the given criteria
     */
    public byte[] generateProductMovementsReport(GenerateMovementReportDto dto, String userId) throws AccessDeniedException, NoMovementsForReportGenerationException {

        // Verify user access to the branch
        this.branchApi.assertUserHasAccessToBranch(userId, dto.branchId());

        // Create the movement report
        MovementReport report = reportsRepository.createProductMovementsReport(
                dto.branchId(),
                dto.categoryId(),
                dto.fromDate(),
                dto.toDate(),
                userId
        );

        // Validate that there are movements in the report
        if (report.getItems().isEmpty()) {
            throw new NoMovementsForReportGenerationException("No se encontraron movimientos para los criterios proporcionados.");
        }

        // Generate and return the PDF using the report entity
        return report.generatePdf(pdfBuilder.getObject(), "Reporte de Movimientos de Inventario de Productos");
    }

    /**
     * Genera el reporte de movimientos de materias primas en formato PDF.
     *
     * @param dto    Data transfer object containing report parameters
     * @param userId ID of the user requesting the report
     * @return Byte array representing the generated PDF report
     * @throws AccessDeniedException                   if the user does not have access to the specified branch
     * @throws NoMovementsForReportGenerationException if no movements are found for the given criteria
     */
    public byte[] generateRawMaterialMovementsReport(GenerateMovementReportDto dto, String userId) throws AccessDeniedException, NoMovementsForReportGenerationException {

        // Verify user access to the branch
        this.branchApi.assertUserHasAccessToBranch(userId, dto.branchId());

        // Create the movement report
        MovementReport report = reportsRepository.createRawMaterialMovementsReport(
                dto.branchId(),
                dto.categoryId(),
                dto.fromDate(),
                dto.toDate(),
                userId
        );

        // Validate that there are movements in the report
        if (report.getItems().isEmpty()) {
            throw new NoMovementsForReportGenerationException("No se encontraron movimientos de materias primas para los criterios proporcionados.");
        }

        // Generate and return the PDF using the report entity
        return report.generatePdf(pdfBuilder.getObject(), "Reporte de Movimientos de Inventario de Materias Primas");
    }

}
