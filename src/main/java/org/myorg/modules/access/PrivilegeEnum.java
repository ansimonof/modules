package org.myorg.modules.access;

public interface PrivilegeEnum {

    String getUniqueKey();

    AccessOpCollection getAvailableOps();

    int intValue();
}
