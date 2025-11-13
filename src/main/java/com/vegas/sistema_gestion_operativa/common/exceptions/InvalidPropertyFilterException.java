package com.vegas.sistema_gestion_operativa.common.exceptions;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InvalidPropertyFilterException extends ApiException {
    public InvalidPropertyFilterException(InvalidDataAccessApiUsageException e) {
        super(buildMessage(e), HttpStatus.BAD_REQUEST);
    }

    private static String buildMessage(InvalidDataAccessApiUsageException e) {
        Pattern pattern = Pattern.compile("Could not resolve attribute '([^']+)'");
        Matcher matcher = pattern.matcher(e.getMessage());
        String invalidProperty = matcher.find() ? matcher.group(1) : "desconocido";
        return "Property '%s' is not valid to order or filter".formatted(invalidProperty);
    }
}
