package com.vegas.sistema_gestion_operativa.users.factory;

import com.vegas.sistema_gestion_operativa.users.domain.IdType;
import org.springframework.stereotype.Component;

@Component
public class IdTypeFactory {

    public IdType createIdType(String idType) throws IllegalArgumentException {
        try {
            return IdType.valueOf(idType);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de identificación invalido: " + idType);
        }
    }
}
