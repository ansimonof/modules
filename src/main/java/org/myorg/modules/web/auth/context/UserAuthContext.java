package org.myorg.modules.web.auth.context;

import org.myorg.modules.web.auth.context.source.UserSource;

public class UserAuthContext extends AuthContext<UserSource> {

    public UserAuthContext(UserSource source) {
        super(source, UserAuthContext.class);
    }

    public UserAuthContext(UserSource source, Class<?> clazz) {
        super(source, clazz);
    }
}
