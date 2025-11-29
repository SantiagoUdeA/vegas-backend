package com.vegas.sistema_gestion_operativa.sales.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DailySalesAmountDto(
        LocalDate date,
        BigDecimal totalAmount
) {}