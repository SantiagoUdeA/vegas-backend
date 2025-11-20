package com.vegas.sistema_gestion_operativa.provider.application.factory;

import com.vegas.sistema_gestion_operativa.provider.application.dto.CreateProviderDto;
import com.vegas.sistema_gestion_operativa.provider.domain.entity.Provider;
import org.springframework.stereotype.Component;

@Component
public class ProviderFactory {

    public Provider createFromDto(CreateProviderDto dto) {
        return Provider.builder()
                .nit(dto.nit())
                .name(dto.name())
                .phoneNumber(dto.phoneNumber())
                .active(true)
                .build();
    }
}
