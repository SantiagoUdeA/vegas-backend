package com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.exceptions;

import com.vegas.sistema_gestion_operativa.common.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class BatchNotFoundException extends ApiException {
    public BatchNotFoundException(Long batchId) {
        super("No se encontró el lote con id: " + batchId, HttpStatus.NOT_FOUND);
    }
}
