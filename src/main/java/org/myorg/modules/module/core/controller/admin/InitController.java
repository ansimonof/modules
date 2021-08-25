package org.myorg.modules.module.core.controller.admin;

import org.myorg.modules.access.annotation.AuthorizedContext;
import org.myorg.modules.exception.ModuleExceptionBuilder;
import org.myorg.modules.module.core.domainobject.user.UserBuilder;
import org.myorg.modules.module.core.domainobject.user.UserDto;
import org.myorg.modules.module.core.domainobject.user.UserMapper;
import org.myorg.modules.module.core.domainobject.user.UserResource;
import org.myorg.modules.module.core.service.user.UserService;
import org.myorg.modules.querypool.QueryPool;
import org.myorg.modules.web.auth.context.UnauthContext;
import org.myorg.modules.web.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;


@RestController
@RequestMapping("/admin/init")
public class InitController {

    @Autowired
    private QueryPool queryPool;

    @Autowired
    private UserService userService;

    @Autowired
    private SessionManager sessionManager;

    @PostMapping
    @AuthorizedContext({ UnauthContext.class })
    public CompletableFuture<ResponseEntity<UserDto>> init(
            @RequestParam("login") String login,
            @RequestParam("password_hash") String passwordHash
    ) {
        return queryPool.executeAndReturnFuture(pc -> {
            UserResource userResource = UserResource.findAll(pc).stream()
                    .findAny()
                    .orElse(null);
            if (userResource != null) {
                throw ModuleExceptionBuilder.buildAppAlreadyInitialized();
            }

            UserBuilder builder = new UserBuilder()
                    .withLogin(login)
                    .withPasswordHash(passwordHash)
                    .withIsAdmin(true)
                    .withIsEnabled(true);
            userResource = userService.create(builder, pc);

            String session = sessionManager.generateSession();
            UserDto userDto = UserMapper.INSTANCE.toDto(userResource);
            userDto.setSession(session);

            sessionManager.setSessionToUser(session, userResource);

            return ResponseEntity.ok(userDto);
        });
    }

}
