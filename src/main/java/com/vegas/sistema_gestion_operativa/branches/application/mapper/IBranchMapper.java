package com.vegas.sistema_gestion_operativa.branches.application.mapper;

import com.vegas.sistema_gestion_operativa.branches.domain.entity.Branch;
import com.vegas.sistema_gestion_operativa.branches.application.dto.UpdateBranchDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface IBranchMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Branch partialUpdate(UpdateBranchDto dto, @MappingTarget Branch branch);

}
