package com.vegas.sistema_gestion_operativa.catalog.provider.application.factory;

import com.vegas.sistema_gestion_operativa.catalog.provider.application.dto.CreateProviderDto;
import com.vegas.sistema_gestion_operativa.catalog.provider.domain.entity.Provider;
import org.springframework.stereotype.Component;

@Component
public class ProviderFactory {

    public Provider createFromDto(CreateProviderDto dto) {
        return Provider.builder()
                .nit(dto.nit())
                .name(dto.name())
                .phoneNumber(dto.phoneNumber())
                .build();
    }
}
