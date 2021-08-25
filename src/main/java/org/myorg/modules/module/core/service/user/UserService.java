package org.myorg.modules.module.core.service.user;

import org.apache.commons.lang3.StringUtils;
import org.myorg.modules.exception.ModuleException;
import org.myorg.modules.exception.ModuleExceptionBuilder;
import org.myorg.modules.module.core.domainobject.user.UserBuilder;
import org.myorg.modules.module.core.domainobject.user.UserResource;
import org.myorg.modules.querypool.database.PersistenceContext;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService {

    public UserResource create(UserBuilder builder, PersistenceContext pc) throws ModuleException {
        if (!builder.isContainLogin() || StringUtils.isEmpty(builder.getLogin())) {
            throw ModuleExceptionBuilder.buildDomainObjectEmptyValueException(UserResource.class, UserResource.LOGIN);
        }

        if (!builder.isContainPasswordHash() || StringUtils.isEmpty(builder.getPasswordHash())) {
            throw ModuleExceptionBuilder.buildDomainObjectEmptyValueException(UserResource.class, UserResource.PASSWORD_HASH);
        }

        if (!builder.isContainEnabled() || builder.getIsEnabled() == null) {
            throw ModuleExceptionBuilder.buildDomainObjectEmptyValueException(UserResource.class, UserResource.IS_ENABLED);
        }

        if (!builder.isContainAdmin() || builder.getIsAdmin() == null) {
            throw ModuleExceptionBuilder.buildDomainObjectEmptyValueException(UserResource.class, UserResource.IS_ADMIN);
        }

        UserResource userResource = new UserResource();
        setFieldsFor(userResource, builder, pc);
        userResource = userResource.save(pc);

        return userResource;
    }

    public UserResource update(long id, UserBuilder builder, PersistenceContext pc) throws ModuleException {
        UserResource userResource = UserResource.get(UserResource.class, id, pc);
        if (userResource == null) {
            throw ModuleExceptionBuilder.buildNotFoundDomainObjectException(UserResource.class, id);
        }

        if (builder.isContainLogin() && StringUtils.isEmpty(builder.getLogin())) {
            throw ModuleExceptionBuilder.buildDomainObjectEmptyValueException(UserResource.class, UserResource.LOGIN);
        }

        if (builder.isContainPasswordHash() && StringUtils.isEmpty(builder.getPasswordHash())) {
            throw ModuleExceptionBuilder.buildDomainObjectEmptyValueException(UserResource.class, UserResource.PASSWORD_HASH);
        }

        if (builder.isContainEnabled() && builder.getIsEnabled() == null) {
            throw ModuleExceptionBuilder.buildDomainObjectEmptyValueException(UserResource.class, UserResource.IS_ENABLED);
        }

        if (builder.isContainAdmin() && builder.getIsAdmin() == null) {
            throw ModuleExceptionBuilder.buildDomainObjectEmptyValueException(UserResource.class, UserResource.IS_ADMIN);
        }

        setFieldsFor(userResource, builder, pc);
        userResource = userResource.update(pc);

        return userResource;
    }

    public void remove(long id, PersistenceContext pc) throws ModuleException {
        UserResource userResource = UserResource.get(UserResource.class, id, pc);
        if (userResource != null) {
            userResource.remove(pc);
        }
    }

    private void setFieldsFor(UserResource userResource,
                              UserBuilder builder,
                              PersistenceContext pc) throws ModuleException {
        if (builder.isContainLogin()) {
            UserResource byLogin = UserResource.findByLogin(builder.getLogin(), pc);
            if (byLogin != null && !Objects.equals(byLogin.getId(), userResource.getId())) {
                throw ModuleExceptionBuilder.buildNotUniqueDomainObjectException(UserResource.class, UserResource.LOGIN,
                        builder.getLogin());
            }
            userResource.setLogin(builder.getLogin());
        }

        if (builder.isContainPasswordHash()) {
            userResource.setPasswordHash(builder.getPasswordHash());
        }

        if (builder.isContainAdmin()) {
            userResource.setAdmin(builder.getIsAdmin());
        }

        if (builder.isContainEnabled()) {
            if (userResource.isAdmin() && !builder.getIsEnabled()) {
                throw ModuleExceptionBuilder.buildAdminCannotBeDisabledException();
            }
            userResource.setEnabled(builder.getIsEnabled());
        }
    }
}
