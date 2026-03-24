package com.vegas.sistema_gestion_operativa.subscription.application.mapper;

import com.vegas.sistema_gestion_operativa.subscription.application.dto.SubscriptionResponseDto;
import com.vegas.sistema_gestion_operativa.subscription.application.dto.UpdateSubscriptionDto;
import com.vegas.sistema_gestion_operativa.subscription.domain.entity.Subscription;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ISubscriptionMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Subscription partialUpdate(UpdateSubscriptionDto dto, @MappingTarget Subscription subscription);

    @Mapping(target = "ownerFullName", ignore = true)
    @Mapping(target = "currentFranchiseCount", ignore = true)
    SubscriptionResponseDto toResponseDto(Subscription subscription);
}
