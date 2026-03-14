package com.vegas.sistema_gestion_operativa.franchise.application.mapper;

import com.vegas.sistema_gestion_operativa.franchise.application.dto.FranchiseResponseDto;
import com.vegas.sistema_gestion_operativa.franchise.application.dto.UpdateFranchiseDto;
import com.vegas.sistema_gestion_operativa.franchise.domain.entity.Franchise;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface IFranchiseMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Franchise partialUpdate(UpdateFranchiseDto dto, @MappingTarget Franchise franchise);

    FranchiseResponseDto toResponseDto(Franchise franchise);
}
