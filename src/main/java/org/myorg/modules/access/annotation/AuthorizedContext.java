package org.myorg.modules.access.annotation;

import org.myorg.modules.web.auth.context.UnauthContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthorizedContext {

    Class<? extends UnauthContext>[] value();
}
