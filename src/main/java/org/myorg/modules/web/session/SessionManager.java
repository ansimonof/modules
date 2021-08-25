package org.myorg.modules.web.session;

import org.myorg.modules.exception.ModuleException;
import org.myorg.modules.module.core.domainobject.user.UserResource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class SessionManager {

    private static final long EXPIRE_TIME = 1 * 60 * 1000;

    //private final Cache<String, Long> sessions;

    private final Map<String, Long> sessions = new HashMap<>();

    public SessionManager() {
      /*  this.sessions = CacheBuilder.newBuilder()
                .expireAfterWrite(EXPIRE_TIME, TimeUnit.MILLISECONDS)
                .build();*/
    }

    public void setSessionToUser(String session, UserResource userResource) {
        sessions.put(session, userResource.getId());
    }

    public Long getUserId(String session) throws ModuleException {
        try {
            return sessions.get(session);
        } catch (Exception e) {
            throw new ModuleException(e.getMessage());
        }
    }

    public void clearSession(String session) {
        sessions.remove(session);
    }

    public String generateSession() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
