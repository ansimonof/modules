package org.myorg.modules.module.core.domainobject.access;

import org.myorg.modules.util.BuilderField;

public class CoreAccessPrivilegeBuilder {

    private BuilderField<Long> userId = new BuilderField<>();
    private BuilderField<Integer> privilege = new BuilderField<>();
    private BuilderField<Integer> ops = new BuilderField<>();

    public CoreAccessPrivilegeBuilder withUserId(Long userId) {
        this.userId.with(userId);
        return this;
    }

    public CoreAccessPrivilegeBuilder withPrivilege(Integer privilege) {
        this.privilege.with(privilege);
        return this;
    }

    public CoreAccessPrivilegeBuilder withOps(Integer ops) {
        this.ops.with(ops);
        return this;
    }

    public boolean isContainUserId() {
        return userId.isContainField();
    }

    public boolean isContainPrivilege() {
        return privilege.isContainField();
    }

    public boolean isContainOps() {
        return privilege.isContainField();
    }

    public Long getUserId() {
        return userId.getField();
    }

    public Integer getPrivilege() {
        return privilege.getField();
    }

    public Integer getOps() {
        return ops.getField();
    }
}
