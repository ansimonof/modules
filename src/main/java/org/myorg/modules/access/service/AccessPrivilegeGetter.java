package org.myorg.modules.access.service;

import org.myorg.modules.access.PrivilegeAccessOpPair;
import org.myorg.modules.exception.ModuleException;
import org.myorg.modules.querypool.database.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccessPrivilegeGetter {

    private final List<? extends AccessPrivilegeService> privilegeServices;

    @Autowired
    public AccessPrivilegeGetter(List<? extends AccessPrivilegeService> privilegeServices) {
        this.privilegeServices = privilegeServices;
    }

    public List<PrivilegeAccessOpPair> getUserPrivileges(long userId, PersistenceContext pc) throws ModuleException {
        List<PrivilegeAccessOpPair> privileges = new ArrayList<>();
        for (AccessPrivilegeService privilegeService : privilegeServices) {
            privileges.addAll(privilegeService.getPrivilegeAccessOpPairs(userId, pc));
        }

        return privileges;
    }
}
