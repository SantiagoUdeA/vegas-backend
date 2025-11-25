package com.vegas.sistema_gestion_operativa.sales.application.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SaleFilterDto {

    Long branchId;          // obligatorio o no, depende de tu negocio
    LocalDate from;         // opcional
    LocalDate to;           // opcional
    String employeeId;      // opcional
}