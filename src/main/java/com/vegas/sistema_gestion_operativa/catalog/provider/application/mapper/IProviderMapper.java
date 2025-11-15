package com.vegas.sistema_gestion_operativa.catalog.provider.application.mapper;

import com.vegas.sistema_gestion_operativa.catalog.provider.domain.entity.Provider;
import com.vegas.sistema_gestion_operativa.catalog.provider.application.dto.UpdateProviderDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface IProviderMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Provider partialUpdate(UpdateProviderDto dto, @MappingTarget Provider provider);

}
