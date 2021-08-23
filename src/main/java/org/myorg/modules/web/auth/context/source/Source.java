package org.myorg.modules.web.auth.context.source;

import org.myorg.modules.access.PrivilegeAccessOpPair;

import java.util.List;

public class Source {

    private List<PrivilegeAccessOpPair> privilegeAccessOpPairs;

    public List<PrivilegeAccessOpPair> getPrivilegeAccessOpPairs() {
        return privilegeAccessOpPairs;
    }

    public void setPrivilegeAccessOpPairs(List<PrivilegeAccessOpPair> privilegeAccessOpPairs) {
        this.privilegeAccessOpPairs = privilegeAccessOpPairs;
    }
}
