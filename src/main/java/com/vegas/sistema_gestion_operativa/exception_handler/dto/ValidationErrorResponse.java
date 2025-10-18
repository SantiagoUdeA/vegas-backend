package com.vegas.sistema_gestion_operativa.exception_handler.dto;

import java.util.List;

public record ValidationErrorResponse(
        int status,
        String message,
        List<FieldErrorResponse> errors,
        String timestamp
) {}