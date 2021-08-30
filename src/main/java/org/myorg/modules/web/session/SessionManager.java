package org.myorg.modules.web.session;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.myorg.modules.exception.ModuleException;
import org.myorg.modules.module.core.domainobject.user.UserResource;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class SessionManager {

    // 6 часов
    private static final long EXPIRE_TIME = 6 * 60 * 1000;

    private final Cache<String, Long> sessions;

    public SessionManager() {
        this.sessions = CacheBuilder.newBuilder()
                .expireAfterWrite(EXPIRE_TIME, TimeUnit.MILLISECONDS)
                .build();
    }

    public void setSessionToUser(String session, UserResource userResource) {
        sessions.put(session, userResource.getId());
    }

    public Long getUserId(String session) throws ModuleException {
        try {
            return sessions.getIfPresent(session);
        } catch (Exception e) {
            throw new ModuleException(e.getMessage());
        }
    }

    public void clearSession(String session) {
        sessions.invalidate(session);
    }

    public String generateSession() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
