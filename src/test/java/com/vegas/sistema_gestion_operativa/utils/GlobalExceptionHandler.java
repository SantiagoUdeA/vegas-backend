package com.vegas.sistema_gestion_operativa.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para las pruebas.
 * Captura las excepciones de seguridad y las convierte en respuestas HTTP apropiadas.
 */
@RestControllerAdvice
@ActiveProfiles("test")
public class GlobalExceptionHandler {

    /**
     * Maneja las excepciones de acceso denegado (cuando falla @PreAuthorize).
     * Retorna HTTP 403 (Forbidden) con un mensaje descriptivo.
     *
     * @param ex la excepción de acceso denegado
     * @return respuesta HTTP 403 con detalles del error
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDeniedException(AccessDeniedException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Access Denied");
        errorResponse.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }
}
