package org.myorg.modules.access;

public class PrivilegeAccessOpPair {

    private PrivilegeEnum privilegeEnum;
    private AccessOpCollection accessOpCollection;

    public PrivilegeAccessOpPair(PrivilegeEnum privilegeEnum, AccessOp... accessOps) {
        this.privilegeEnum = privilegeEnum;
        this.accessOpCollection = new AccessOpCollection(accessOps);
    }

    public PrivilegeAccessOpPair(PrivilegeEnum privilegeEnum, AccessOpCollection accessOpCollection) {
        this.privilegeEnum = privilegeEnum;
        this.accessOpCollection = accessOpCollection;
    }

    public PrivilegeEnum getPrivilegeEnum() {
        return privilegeEnum;
    }

    public void setPrivilegeEnum(PrivilegeEnum privilegeEnum) {
        this.privilegeEnum = privilegeEnum;
    }

    public AccessOpCollection getAccessOpCollection() {
        return accessOpCollection;
    }

    public void setAccessOpCollection(AccessOpCollection accessOpCollection) {
        this.accessOpCollection = accessOpCollection;
    }
}