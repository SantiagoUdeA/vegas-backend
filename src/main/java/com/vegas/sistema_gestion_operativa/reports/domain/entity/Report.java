package com.vegas.sistema_gestion_operativa.reports.domain.entity;

import com.vegas.sistema_gestion_operativa.reports.domain.builder.IPdfBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public abstract class Report {

    protected final String branchName;
    protected final String userName;
    protected final String userRole;

    public abstract byte[] generatePdf(IPdfBuilder pdfBuilder, String title);
}
