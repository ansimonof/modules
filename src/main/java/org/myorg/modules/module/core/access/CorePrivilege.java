package org.myorg.modules.module.core.access;

import org.myorg.modules.access.AccessOp;
import org.myorg.modules.access.AccessOpCollection;
import org.myorg.modules.access.Privilege;
import org.myorg.modules.access.PrivilegeEnum;

public enum CorePrivilege implements PrivilegeEnum {

    USER_MANAGEMENT(CorePrivilegeConsts.USER_MANAGEMENT_ID,
            "core.privilege.user_management",
            AccessOp.READ, AccessOp.WRITE);

    private Privilege privilege;

    CorePrivilege(int id, String uniqueName, AccessOp... ops) {
        privilege = new Privilege(id, uniqueName, ops);
    }

    @Override
    public String getUniqueKey() {
        return privilege.getUniqueKey();
    }

    @Override
    public AccessOpCollection getAvailableOps() {
        return privilege.getAccessOpCollection();
    }

    @Override
    public int intValue() {
        return privilege.intValue();
    }

    public static CorePrivilege valueOf(int id) {
        for (CorePrivilege value : values()) {
            if (value.intValue() == id) {
                return value;
            }
        }
        return null;
    }


}
