package org.myorg.modules.web.converter;

import org.myorg.modules.access.PrivilegeEnum;
import org.myorg.modules.access.service.AccessPrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PrivilegeEnumConverter implements Converter<String, PrivilegeEnum> {

    @Autowired
    private AccessPrivilegeService accessPrivilegeService;

    @Override
    public PrivilegeEnum convert(String source) {
        return accessPrivilegeService.getPrivilegeByUniqueKey(source);
    }
}
