package org.myorg.modules.web.auth.authorizer;

import org.myorg.modules.access.PrivilegeAccessOpPair;
import org.myorg.modules.access.service.AccessPrivilegeGetter;
import org.myorg.modules.exception.ModuleException;
import org.myorg.modules.module.core.domainobject.user.UserResource;
import org.myorg.modules.querypool.QueryPool;
import org.myorg.modules.querypool.database.PersistenceContext;
import org.myorg.modules.web.auth.context.UnauthContext;
import org.myorg.modules.web.auth.context.UserAuthContext;
import org.myorg.modules.web.auth.context.source.Source;
import org.myorg.modules.web.auth.context.source.UserSource;
import org.myorg.modules.web.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Service
public class UserSessionAuthorizer implements Authorizer {

    private static final String SESSION = "session";

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private AccessPrivilegeGetter accessPrivilegeGetter;

    @Autowired
    private QueryPool queryPool;

    @Override
    public UnauthContext<? extends Source> auth(HttpServletRequest request) {
        // Нельзя передавать request в новый поток
        String session = getSession(request);
        return queryPool.execute(pc -> {
            Long userId = sessionManager.getUserId(session);
            if (userId == null) {
                return null;
            }

            UserResource userResource = UserResource.get(UserResource.class, userId, pc);
            // Возможно юзер был удален
            if (userResource == null) {
                return null;
            }

            UserSource source = getUserResource(userResource, pc);
            return new UserAuthContext(source);
        }).join();
    }

    @Override
    public boolean isSupport(HttpServletRequest request) {
        return getSession(request) != null;
    }

    private UserSource getUserResource(UserResource userResource, PersistenceContext pc) throws ModuleException {
        UserSource userSource = new UserSource();
        userSource.setId(userResource.getId());
        userResource.setAdmin(userResource.isAdmin());
        userResource.setEnabled(userResource.isEnabled());

        List<PrivilegeAccessOpPair> userPrivileges = accessPrivilegeGetter.getUserPrivileges(userResource.getId(), pc);
        userSource.setPrivilegeAccessOpPairs(userPrivileges);

        return userSource;
    }

    private String getSession(HttpServletRequest request) {
        String session = request.getParameter(SESSION);
        if (session == null) {
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if (Objects.equals(cookie.getName(), SESSION)) {
                        return cookie.getValue();
                    }
                }
            }
        }

        return session;
    }

}
