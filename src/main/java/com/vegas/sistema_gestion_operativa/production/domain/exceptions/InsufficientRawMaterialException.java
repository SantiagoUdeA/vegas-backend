package com.vegas.sistema_gestion_operativa.production.domain.exceptions;

import com.vegas.sistema_gestion_operativa.production.application.dto.RawMaterialShortageDto;
import lombok.Getter;

import java.util.List;

@Getter
public class InsufficientRawMaterialException extends RuntimeException {

    private final List<RawMaterialShortageDto> shortages;

    public InsufficientRawMaterialException(List<RawMaterialShortageDto> shortages) {
        super("No hay suficiente materia prima para completar la producción.");
        this.shortages = shortages;
    }
}
