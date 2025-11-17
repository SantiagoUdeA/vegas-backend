package com.vegas.sistema_gestion_operativa.exception_handler.configuration;

import com.vegas.sistema_gestion_operativa.common.exceptions.ApiException;
import com.vegas.sistema_gestion_operativa.exception_handler.dto.FieldErrorResponse;
import com.vegas.sistema_gestion_operativa.exception_handler.dto.ValidationErrorResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Global exception handler for REST controllers.
 * Handles validation errors and other exceptions.
 */
@RestControllerAdvice
@Profile({"dev", "prod", "test"})
public class GlobalExceptionHandler {

    /**
     * Handles custom ApiException and returns its status and message.
     * @param ex the ApiException thrown
     * @return ResponseEntity with status and message from the exception
     */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, String>> handleApiExceptions(
            ApiException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }

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

    /**
     * Handles validation errors when @Valid fails on DTOs.
     * @param ex the exception thrown by validation
     * @return a map with field errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<FieldErrorResponse> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new FieldErrorResponse(error.getField(), error.getDefaultMessage()))
                .toList();

        ValidationErrorResponse response = new ValidationErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validación fallida",
                fieldErrors,
                Instant.now().toString()
        );

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Maneja excepciones de integridad de datos (violaciones de constraints, claves únicas, etc.)
     * Retorna HTTP 409 (Conflict) con información detallada.
     *
     * @param ex excepción lanzada por JPA/Hibernate
     * @return respuesta estructurada con detalles del error
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ValidationErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        // Mensaje más claro según el tipo de violación
        String message = "Violación de integridad de datos.";

        // Se extrae el mensaje detallado de la causa raíz
        String detailedMessage = ex.getMostSpecificCause().getMessage();

        // Construye la respuesta de error
        ValidationErrorResponse response = new ValidationErrorResponse(
                HttpStatus.CONFLICT.value(),
                message,
                List.of(new FieldErrorResponse("database", detailedMessage)),
                Instant.now().toString()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /**
     * Handles RuntimeException (e.g., business logic errors like "User already exists").
     * @param ex the runtime exception
     * @return error message with HTTP 500
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        ex.printStackTrace();
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

}
