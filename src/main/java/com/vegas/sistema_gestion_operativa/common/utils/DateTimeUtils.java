package com.vegas.sistema_gestion_operativa.common.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Utilidad para manejar fechas y horas en la zona horaria de Bogotá
 */
public class DateTimeUtils {

    public static final ZoneId BOGOTA_ZONE = ZoneId.of("America/Bogota");

    private DateTimeUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Obtiene la fecha y hora actual en la zona horaria de Bogotá
     */
    public static LocalDateTime nowInBogota() {
        return ZonedDateTime.now(BOGOTA_ZONE).toLocalDateTime();
    }

    /**
     * Obtiene la fecha actual en la zona horaria de Bogotá
     */
    public static LocalDate todayInBogota() {
        return ZonedDateTime.now(BOGOTA_ZONE).toLocalDate();
    }

    /**
     * Convierte una fecha a las 00:00:00 en zona horaria de Bogotá
     */
    public static LocalDateTime toStartOfDay(LocalDate date) {
        if (date == null) return null;
        return date.atStartOfDay();
    }

    /**
     * Convierte una fecha a las 23:59:59 en zona horaria de Bogotá
     */
    public static LocalDateTime toEndOfDay(LocalDate date) {
        if (date == null) return null;
        return date.atTime(23, 59, 59);
    }

    /**
     * Convierte un LocalDateTime a las 00:00:00 del mismo día
     */
    public static LocalDateTime toStartOfDay(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        return dateTime.toLocalDate().atStartOfDay();
    }

    /**
     * Convierte un LocalDateTime a las 23:59:59 del mismo día
     */
    public static LocalDateTime toEndOfDay(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        return dateTime.toLocalDate().atTime(23, 59, 59);
    }

    /**
     * Formatea un LocalDateTime para reportes en el formato "dd/MM/yyyy HH:mm"
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return dateTime.format(formatter);
    }

    /**
     * Formatea un LocalDate para reportes en el formato "dd/MM/yyyy"
     */
    public static String formatDate(LocalDateTime date) {
        if (date == null) {
            return "";
        }
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }
}

