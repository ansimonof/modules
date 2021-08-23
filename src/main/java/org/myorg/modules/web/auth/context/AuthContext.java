package org.myorg.modules.web.auth.context;

import org.myorg.modules.web.auth.context.source.Source;

public abstract class AuthContext<S extends Source> extends UnauthContext<S> {

    public AuthContext(S source, Class<?> clazz) {
        super(source, clazz);
    }
}
