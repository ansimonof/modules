package org.myorg.modules.access.service;

import org.myorg.modules.access.AccessOp;
import org.myorg.modules.access.AccessOpCollection;
import org.myorg.modules.access.PrivilegeAccessOpPair;
import org.myorg.modules.access.PrivilegeEnum;
import org.myorg.modules.access.domainobject.AccessPrivilegeResource;
import org.myorg.modules.exception.ModuleException;
import org.myorg.modules.exception.ModuleExceptionBuilder;
import org.myorg.modules.module.core.domainobject.user.UserResource;
import org.myorg.modules.querypool.database.PersistenceContext;
import org.reflections.Reflections;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AccessPrivilegeService {

    private Set<PrivilegeEnum> privileges;

    public AccessPrivilegeService() {
        initPrivileges();
    }

    public List<PrivilegeAccessOpPair> getUserPrivileges(long userId, PersistenceContext pc) throws ModuleException {
        return AccessPrivilegeResource.getAllPrivilegesByUserId(userId, pc).stream()
                .map(apr -> new PrivilegeAccessOpPair(
                        getPrivilegeByUniqueKey(apr.getUniqueKey()),
                        new AccessOpCollection(apr.getOps())))
                .collect(Collectors.toList());
    }

    public Set<PrivilegeEnum> getAllPrivileges() {
        return new HashSet<>(privileges);
    }

    public PrivilegeEnum getPrivilegeByUniqueKey(String uniqueKey) {
        return privileges.stream()
                .filter(p -> Objects.equals(p.getUniqueKey(), uniqueKey))
                .findAny()
                .orElse(null);
    }

    public void updatePrivilegesForUser(long userId,
                                       PrivilegeEnum privilegeEnum,
                                       AccessOp[] accessOps,
                                       PersistenceContext pc) throws ModuleException {
        UserResource userResource = UserResource.get(UserResource.class, userId, pc);
        if (userResource == null) {
            throw ModuleExceptionBuilder.buildNotFoundDomainObjectException(UserResource.class, userId);
        }

        for (AccessOp accessOp : accessOps) {
            if (!privilegeEnum.getAvailableOps().contains(accessOp)) {
                throw ModuleExceptionBuilder.buildPrivilegeDoesNotHaveAccessOpException(privilegeEnum, accessOp);
            }
        }

        AccessPrivilegeResource accessPrivilegeResource = AccessPrivilegeResource.getAllPrivilegesByUserId(
                userId, pc)
                .stream()
                .filter(ap -> Objects.equals(privilegeEnum.getUniqueKey(),ap.getUniqueKey()))
                .findAny()
                .orElse(null);
        if (accessOps.length == 0 && accessPrivilegeResource == null) {
            return;
        }

        if (accessOps.length == 0) {
            accessPrivilegeResource.remove(pc);
            return;
        }

        if (accessPrivilegeResource == null) {
            accessPrivilegeResource = new AccessPrivilegeResource();
            accessPrivilegeResource.setUserResource(userResource);
            accessPrivilegeResource.setUniqueKey(privilegeEnum.getUniqueKey());
            accessPrivilegeResource.setOps(new AccessOpCollection(accessOps).getValue());
            accessPrivilegeResource.save(pc);
            return;
        }


        int value = new AccessOpCollection(accessOps).getValue();
        if (value != accessPrivilegeResource.getOps()) {
            accessPrivilegeResource.setOps(value);
            accessPrivilegeResource.update(pc);
        }
    }

    private void initPrivileges() {
        privileges = new HashSet<>();
        for (Class<? extends PrivilegeEnum> privilegeEnum : new Reflections("org.myorg")
                .getSubTypesOf(PrivilegeEnum.class)) {
            if (privilegeEnum.isEnum()) {
                privileges.addAll(Arrays.asList(privilegeEnum.getEnumConstants()));
            }
        }
        checkPrivileges();
    }

    private void checkPrivileges() {
        int uniqueKeySetSize = privileges.stream()
                .map(PrivilegeEnum::getUniqueKey)
                .collect(Collectors.toSet())
                .size();
        if (uniqueKeySetSize != privileges.size()) {
            throw new RuntimeException("There is nonunique key for privilege");
        }
    }
}
