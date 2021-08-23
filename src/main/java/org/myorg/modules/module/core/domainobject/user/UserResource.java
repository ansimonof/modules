package org.myorg.modules.module.core.domainobject.user;

import org.myorg.modules.querypool.database.DomainObject;
import org.myorg.modules.querypool.database.PersistenceContext;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user_",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = UserResource.LOGIN)
    })
@NamedQueries({
        @NamedQuery(name = "UserResource.findAll",
                query = "select u from UserResource u"),
        @NamedQuery(name = "UserResource.findByLogin",
                query = "select u from UserResource u where u.login = :login")
})
public class UserResource extends DomainObject<UserResource> {

    public static final String LOGIN = "login";

    public static final String PASSWORD_HASH = "password_hash";

    public static final String IS_ENABLED = "is_enabled";

    public static final String IS_ADMIN = "is_admin";

    @Column(name = LOGIN, nullable = false)
    private String login;

    @Column(name = PASSWORD_HASH, nullable = false)
    private String passwordHash;

    @Column(name = IS_ENABLED, nullable = false)
    private Boolean isEnabled;

    @Column(name = IS_ADMIN, nullable = false)
    private Boolean isAdmin;

    public static UserResource findByLogin(String login, PersistenceContext pc) {
        return createNamedQuery(UserResource.class, "UserResource.findByLogin", pc)
                .setParameter(LOGIN, login)
                .getResultList()
                .stream()
                .findAny()
                .orElse(null);
    }

    public static List<UserResource> findAll(PersistenceContext pc) {
        return createNamedQuery(UserResource.class, "UserResource.findAll", pc)
                .getResultList();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public Boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
