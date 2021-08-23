package org.myorg.modules.module.core.domainobject.user;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toDto(UserResource userResource);
    List<UserDto> toDtos(List<UserResource> userResources);
}
