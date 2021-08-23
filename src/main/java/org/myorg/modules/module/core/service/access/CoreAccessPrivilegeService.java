package org.myorg.modules.module.core.service.access;

import org.myorg.modules.access.AccessOp;
import org.myorg.modules.access.AccessOpCollection;
import org.myorg.modules.access.PrivilegeAccessOpPair;
import org.myorg.modules.access.service.AccessPrivilegeService;
import org.myorg.modules.exception.ModuleException;
import org.myorg.modules.module.core.access.CorePrivilege;
import org.myorg.modules.module.core.domainobject.access.CoreAccessPrivilegeResource;
import org.myorg.modules.module.core.domainobject.user.UserResource;
import org.myorg.modules.querypool.database.PersistenceContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CoreAccessPrivilegeService implements AccessPrivilegeService {

    @Override
    public List<PrivilegeAccessOpPair> getPrivilegeAccessOpPairs(long userId, PersistenceContext pc) throws ModuleException {
        List<CoreAccessPrivilegeResource> coreAccessPrivilegeResources = CoreAccessPrivilegeResource
                .getAllPrivilegesByUserId(userId, pc);

        return coreAccessPrivilegeResources.stream()
                .map(ap -> new PrivilegeAccessOpPair(CorePrivilege.valueOf(ap.getPrivilege()),
                        new AccessOpCollection(ap.getOps())))
                .collect(Collectors.toList());
    }

    public void updatePrivilegesToUser(UserResource userResource,
                                       CorePrivilege corePrivilege,
                                       AccessOp[] accessOps,
                                       PersistenceContext pc) throws ModuleException {
        CoreAccessPrivilegeResource savedCorePrivilege = CoreAccessPrivilegeResource.getAllPrivilegesByUserId(
                userResource.getId(), pc)
                .stream()
                .filter(ap -> corePrivilege.intValue() == ap.getPrivilege())
                .findAny()
                .orElse(null);
        if (accessOps.length == 0 && savedCorePrivilege == null) {
            return;
        }

        if (accessOps.length == 0) {
            savedCorePrivilege.remove(pc);
            return;
        }

        if (savedCorePrivilege == null) {
            savedCorePrivilege = new CoreAccessPrivilegeResource();
            savedCorePrivilege.setUserResource(userResource);
            savedCorePrivilege.setOps(new AccessOpCollection(accessOps).getValue());
            savedCorePrivilege.setPrivilege(corePrivilege.intValue());
            savedCorePrivilege.save(pc);
            return;
        }

        savedCorePrivilege.setOps(new AccessOpCollection(accessOps).getValue());
        savedCorePrivilege.update(pc);
    }
}
