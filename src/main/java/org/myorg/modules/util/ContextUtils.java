package org.myorg.modules.util;

import org.myorg.modules.web.auth.context.Context;

import java.lang.reflect.InvocationTargetException;

public class ContextUtils {

    public static Context<?> copy(Context<?> context) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return (Context<?>) context.getClazz().getConstructor(
                context.getSource().getClass())
                .newInstance(context.getSource());
    }
}
