package org.myorg.modules.access.domainobject;

import org.myorg.modules.module.core.domainobject.user.UserResource;
import org.myorg.modules.querypool.database.DomainObject;
import org.myorg.modules.querypool.database.PersistenceContext;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(
        name = "access_privilege",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {
                        AccessPrivilegeResource.UNIQUE_KEY,
                        AccessPrivilegeResource.USER_ID
                })
})
@NamedQueries({
        @NamedQuery(name = "AccessPrivilege.findAllByUserId",
                query = "select ap from AccessPrivilegeResource ap where ap.userResource.id = :user_id")
})
public class AccessPrivilegeResource extends DomainObject<AccessPrivilegeResource> {

    public static final String USER_ID = "user_id";

    public static final String UNIQUE_KEY = "unique_key";

    public static final String OPS = "ops";

    @ManyToOne
    @JoinColumn(name = USER_ID, nullable = false)
    private UserResource userResource;

    @Column(name = UNIQUE_KEY, nullable = false)
    private String uniqueKey;

    @Column(name = OPS, nullable =  false)
    private Integer ops;

    public static List<AccessPrivilegeResource> getAllPrivilegesByUserId(long userId, PersistenceContext pc) {
        return AccessPrivilegeResource.createNamedQuery(
                AccessPrivilegeResource.class,
                "AccessPrivilege.findAllByUserId",
                pc)
                .setParameter(USER_ID, userId)
                .getResultList();
    }

    public UserResource getUserResource() {
        return userResource;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public Integer getOps() {
        return ops;
    }

    public void setUserResource(UserResource userResource) {
        this.userResource = userResource;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public void setOps(Integer ops) {
        this.ops = ops;
    }
}

