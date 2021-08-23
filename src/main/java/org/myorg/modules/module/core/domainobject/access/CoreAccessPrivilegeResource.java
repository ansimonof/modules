package org.myorg.modules.module.core.domainobject.access;

import org.myorg.modules.module.core.domainobject.user.UserResource;
import org.myorg.modules.querypool.database.DomainObject;
import org.myorg.modules.querypool.database.PersistenceContext;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "core_access_privilege")
@NamedQueries({
        @NamedQuery(name = "CoreAccessPrivilege.findAllByUserId",
            query = "select ap from CoreAccessPrivilegeResource ap where ap.userResource.id = :user_id")
})
public class CoreAccessPrivilegeResource extends DomainObject<CoreAccessPrivilegeResource> {

    public static final String USER_ID = "user_id";

    public static final String PRIVILEGE = "privilege";

    public static final String OPS = "ops";

    @ManyToOne
    @JoinColumn(name = USER_ID, nullable = false)
    private UserResource userResource;

    @Column(name = PRIVILEGE, nullable = false)
    private Integer privilege;

    @Column(name = OPS, nullable =  false)
    private Integer ops;

    public static List<CoreAccessPrivilegeResource> getAllPrivilegesByUserId(long userId, PersistenceContext pc) {
        return CoreAccessPrivilegeResource.createNamedQuery(
                CoreAccessPrivilegeResource.class,
                "CoreAccessPrivilege.findAllByUserId",
                pc)
                .setParameter(USER_ID, userId)
                .getResultList();
    }

    public UserResource getUserResource() {
        return userResource;
    }

    public void setUserResource(UserResource userResource) {
        this.userResource = userResource;
    }

    public Integer getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Integer privilege) {
        this.privilege = privilege;
    }

    public Integer getOps() {
        return ops;
    }

    public void setOps(Integer ops) {
        this.ops = ops;
    }
}
