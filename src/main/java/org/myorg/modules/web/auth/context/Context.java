package org.myorg.modules.web.auth.context;

import org.myorg.modules.web.auth.context.source.Source;

public abstract class Context<S extends Source> {

    private S source;

    // Необходим, что бы воссоздать класс из CGLIB proxy
    private Class<?> clazz;

    public Context(S source, Class<?> clazz) {
        this.source = source;
        this.clazz = clazz;
    }

    public S getSource() {
        return source;
    }

    public Class<?> getClazz() {
        return clazz;
    }
}
