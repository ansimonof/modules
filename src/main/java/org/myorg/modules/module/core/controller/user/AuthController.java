package org.myorg.modules.module.core.controller.user;

import org.myorg.modules.web.annotation.AuthorizedContext;
import org.myorg.modules.exception.ModuleExceptionBuilder;
import org.myorg.modules.module.core.domainobject.user.UserBuilder;
import org.myorg.modules.module.core.domainobject.user.UserDto;
import org.myorg.modules.module.core.domainobject.user.UserMapper;
import org.myorg.modules.module.core.domainobject.user.UserResource;
import org.myorg.modules.module.core.service.user.UserService;
import org.myorg.modules.querypool.QueryPool;
import org.myorg.modules.web.auth.context.AuthContext;
import org.myorg.modules.web.auth.context.UnauthContext;
import org.myorg.modules.web.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/core/auth")
public class AuthController {

    @Autowired
    private QueryPool queryPool;

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private UserService userService;

    @PostMapping("/login/session")
    @ResponseStatus(HttpStatus.OK)
    @AuthorizedContext({ UnauthContext.class })
    public CompletableFuture<UserDto> authSession(
            @RequestParam("login") String login,
            @RequestParam("password_hash") String passwordHash
    ) {
        return queryPool.executeAndReturnFuture(pc -> {
            UserResource userResource = UserResource.findByLogin(login, pc);
            if (userResource == null) {
                throw ModuleExceptionBuilder.buildNotFoundDomainObjectException(UserResource.class,
                        UserResource.LOGIN, login);
            }

            if (!Objects.equals(passwordHash, userResource.getPasswordHash())) {
                throw ModuleExceptionBuilder.buildAccessDeniedException();
            }

            String session = sessionManager.generateSession();
            UserDto userDto = UserMapper.INSTANCE.toDto(userResource);
            userDto.setSession(session);

            sessionManager.setSessionToUser(session, userResource);

            return userDto;
        });
    }

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.OK)
    @AuthorizedContext({ UnauthContext.class })
    public CompletableFuture<UserDto> registration(
            @RequestParam("login") String login,
            @RequestParam("password_hash") String passwordHash
    ) {
        return queryPool.executeAndReturnFuture(pc -> {
            UserResource userResource = UserResource.findByLogin(login, pc);
            if (userResource != null) {
                throw ModuleExceptionBuilder.buildNotUniqueDomainObjectException(UserResource.class,
                        UserResource.LOGIN, login);
            }

            userResource = userService.create(
                    new UserBuilder()
                        .withLogin(login)
                        .withPasswordHash(passwordHash)
                        .withIsAdmin(false)
                        .withIsEnabled(true),
                    pc);

            return UserMapper.INSTANCE.toDto(userResource);
        });
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    @AuthorizedContext({ AuthContext.class })
    public CompletableFuture<Void> logout(
            @RequestParam("session") String session
    ) {
        return queryPool.executeAndReturnFuture(pc -> {
            sessionManager.clearSession(session);
            return null;
        });
    }

}
