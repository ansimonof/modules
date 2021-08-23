package org.myorg.modules.web;

import org.myorg.modules.access.AccessOp;
import org.myorg.modules.access.PrivilegeEnum;
import org.myorg.modules.web.auth.context.UnauthContext;

import java.util.Set;

public class ControllerInfo {

    private PrivilegeEnum privilegeEnum;
    private AccessOp[] accessOps;
    private Set<Class<? extends UnauthContext>> authContexts;

    public ControllerInfo(PrivilegeEnum privilegeEnum,
                          AccessOp[] accessOps,
                          Set<Class<? extends UnauthContext>> authContexts) {
        this.privilegeEnum = privilegeEnum;
        this.accessOps = accessOps;
        this.authContexts = authContexts;
    }

    public ControllerInfo() {
    }

    public PrivilegeEnum getPrivilegeEnum() {
        return privilegeEnum;
    }

    public AccessOp[] getAccessOps() {
        return accessOps;
    }

    public Set<Class<? extends UnauthContext>> getAuthContexts() {
        return authContexts;
    }

    public void setPrivilegeEnum(PrivilegeEnum privilegeEnum) {
        this.privilegeEnum = privilegeEnum;
    }

    public void setAccessOps(AccessOp[] accessOps) {
        this.accessOps = accessOps;
    }

    public void setAuthContexts(Set<Class<? extends UnauthContext>> authContexts) {
        this.authContexts = authContexts;
    }
}
