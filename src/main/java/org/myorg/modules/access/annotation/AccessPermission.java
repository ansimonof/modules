package org.myorg.modules.access.annotation;

import org.myorg.modules.access.AccessOp;
import org.myorg.modules.access.PrivilegeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessPermission {

    Class<? extends PrivilegeEnum> privilege();
    int id();
    AccessOp[] ops();
}
