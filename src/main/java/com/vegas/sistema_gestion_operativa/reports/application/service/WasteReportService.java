package com.vegas.sistema_gestion_operativa.reports.application.service;

import com.vegas.sistema_gestion_operativa.branches.IBranchApi;
import com.vegas.sistema_gestion_operativa.common.exceptions.AccessDeniedException;
import com.vegas.sistema_gestion_operativa.reports.application.dto.GenerateWasteReportDto;
import com.vegas.sistema_gestion_operativa.reports.domain.entity.WasteReport;
import com.vegas.sistema_gestion_operativa.reports.domain.exceptions.NoMovementsForReportGenerationException;
import com.vegas.sistema_gestion_operativa.reports.domain.repository.IReportsRepository;
import com.vegas.sistema_gestion_operativa.reports.infrastructure.builder.PdfBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WasteReportService {

    private final IReportsRepository reportsRepository;
    private final IBranchApi branchApi;
    private final ObjectProvider<PdfBuilder> pdfBuilder;

    public byte[] generateWasteReport(GenerateWasteReportDto dto, String userId) throws AccessDeniedException, NoMovementsForReportGenerationException {
        // Verify user access to the branch
        this.branchApi.assertUserHasAccessToBranch(userId, dto.branchId());

        WasteReport report = reportsRepository.createWasteReport(
                dto.branchId(),
                dto.fromDate(),
                dto.toDate(),
                userId
        );

        return report.generatePdf(
                pdfBuilder.getObject(),
                "Reporte de Mermas y Ajustes"
        );
    }
}
