package org.myorg.modules.module.core.domainobject.access;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CoreAccessPrivilegeMapper {

    CoreAccessPrivilegeMapper INSTANCE = Mappers.getMapper(CoreAccessPrivilegeMapper.class);

    CoreAccessPrivilegeDto toDto(CoreAccessPrivilegeResource coreAccessPrivilegeResource);
    List<CoreAccessPrivilegeDto> toDtos(List<CoreAccessPrivilegeResource> coreAccessPrivilegeResource);
}
