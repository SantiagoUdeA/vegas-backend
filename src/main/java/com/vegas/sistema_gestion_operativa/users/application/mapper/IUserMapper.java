package com.vegas.sistema_gestion_operativa.users.application.mapper;

import com.vegas.sistema_gestion_operativa.users.application.dto.UserDto;
import com.vegas.sistema_gestion_operativa.users.domain.entity.User;
import com.vegas.sistema_gestion_operativa.users.application.dto.UpdateUserDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface IUserMapper {

    User toEntity(UserDto userDto);

    UserDto toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UpdateUserDto dto, @MappingTarget User user);

}
