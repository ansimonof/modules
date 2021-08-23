package org.myorg.modules.web.auth.context;

import org.myorg.modules.web.auth.context.source.Source;

public class UnauthContext<S extends Source> extends Context<S> {

    public UnauthContext(S source) {
        super(source, UnauthContext.class);
    }

    public UnauthContext(S source, Class<?> clazz) {
        super(source, clazz);
    }
}
