package org.myorg.modules.module.core.controller.user;

import org.myorg.modules.access.annotation.AuthorizedContext;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private QueryPool queryPool;

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private UserService userService;

    @AuthorizedContext({ UnauthContext.class })
    @PostMapping("/login/session")
    public CompletableFuture<ResponseEntity<UserDto>> authSession(
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

            return ResponseEntity.ok(userDto);
        });
    }

    @AuthorizedContext({ UnauthContext.class })
    @PostMapping("/registration")
    public CompletableFuture<ResponseEntity<UserDto>> registration(
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

            return ResponseEntity.ok(UserMapper.INSTANCE.toDto(userResource));
        });
    }

    @AuthorizedContext({ AuthContext.class })
    @PostMapping("/logout")
    public CompletableFuture<ResponseEntity<Void>> logout(
            @RequestParam("session") String session
    ) {
        return queryPool.executeAndReturnFuture(pc -> {
            sessionManager.clearSession(session);
            return new ResponseEntity<>(HttpStatus.OK);
        });
    }

}
