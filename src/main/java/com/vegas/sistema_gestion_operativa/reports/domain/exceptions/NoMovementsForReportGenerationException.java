package com.vegas.sistema_gestion_operativa.reports.domain.exceptions;

import com.vegas.sistema_gestion_operativa.common.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class NoMovementsForReportGenerationException extends ApiException {
    public NoMovementsForReportGenerationException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
