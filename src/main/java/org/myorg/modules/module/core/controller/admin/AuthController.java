package org.myorg.modules.module.core.controller.admin;

import org.myorg.modules.access.annotation.AuthorizedContext;
import org.myorg.modules.exception.ModuleException;
import org.myorg.modules.module.core.domainobject.user.UserDto;
import org.myorg.modules.module.core.domainobject.user.UserMapper;
import org.myorg.modules.module.core.domainobject.user.UserResource;
import org.myorg.modules.querypool.QueryPool;
import org.myorg.modules.web.auth.context.UnauthContext;
import org.myorg.modules.web.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/admin/auth")
public class AuthController {

    @Autowired
    private QueryPool queryPool;

    @Autowired
    private SessionManager sessionManager;

    @AuthorizedContext({ UnauthContext.class })
    @PostMapping("/session")
    public CompletableFuture<ResponseEntity<UserDto>> authSession(
            @RequestParam("login") String login,
            @RequestParam("password_hash") String passwordHash
    ) {
        return queryPool.executeAndReturnFuture(pc -> {
            UserResource userResource = UserResource.findByLogin(login, pc);
            if (userResource == null) {
                throw new ModuleException("asd");
            }

            if (!Objects.equals(passwordHash, userResource.getPasswordHash())) {
                throw new ModuleException("asd");
            }

            String session = sessionManager.generateSession();
            UserDto userDto = UserMapper.INSTANCE.toDto(userResource);
            userDto.setSession(session);

            sessionManager.setSessionToUser(session, userResource);

            return ResponseEntity.ok(userDto);
        });
    }
}
