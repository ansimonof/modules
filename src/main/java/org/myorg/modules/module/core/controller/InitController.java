package org.myorg.modules.module.core.controller;

import org.myorg.modules.exception.ModuleExceptionBuilder;
import org.myorg.modules.module.core.domainobject.user.UserBuilder;
import org.myorg.modules.module.core.domainobject.user.UserDto;
import org.myorg.modules.module.core.domainobject.user.UserMapper;
import org.myorg.modules.module.core.domainobject.user.UserResource;
import org.myorg.modules.module.core.service.user.UserService;
import org.myorg.modules.querypool.QueryPool;
import org.myorg.modules.web.annotation.AuthorizedContext;
import org.myorg.modules.web.auth.context.UnauthContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;


@RestController
@RequestMapping("/core/init")
public class InitController {

    @Autowired
    private QueryPool queryPool;

    @Autowired
    private UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @AuthorizedContext({ UnauthContext.class })
    public CompletableFuture<UserDto> init(
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

            return UserMapper.INSTANCE.toDto(userResource);
        });
    }

}
