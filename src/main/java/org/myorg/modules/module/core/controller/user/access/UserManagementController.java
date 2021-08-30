package org.myorg.modules.module.core.controller.user.access;

import org.myorg.modules.access.AccessOp;
import org.myorg.modules.access.annotation.AccessPermission;
import org.myorg.modules.module.core.access.CorePrivilege;
import org.myorg.modules.module.core.access.CorePrivilegeConsts;
import org.myorg.modules.module.core.domainobject.user.UserBuilder;
import org.myorg.modules.module.core.domainobject.user.UserDto;
import org.myorg.modules.module.core.domainobject.user.UserMapper;
import org.myorg.modules.module.core.domainobject.user.UserResource;
import org.myorg.modules.module.core.service.user.UserService;
import org.myorg.modules.querypool.QueryPool;
import org.myorg.modules.web.annotation.AuthorizedContext;
import org.myorg.modules.web.auth.context.AuthContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/core/access/user")
public class UserManagementController {

    @Autowired
    private QueryPool queryPool;

    @Autowired
    private UserService userService;

    @PostMapping("/disable")
    @ResponseStatus(HttpStatus.OK)
    @AuthorizedContext({ AuthContext.class })
    @AccessPermission(privilege = CorePrivilege.class,
            id = CorePrivilegeConsts.USER_MANAGEMENT_ID,
            ops = { AccessOp.WRITE })
    public CompletableFuture<UserDto> disableUser(
            @RequestParam("id") Long id
    ) {
        return queryPool.executeAndReturnFuture(pc -> {
            UserResource userResource = userService.update(id, new UserBuilder().withIsEnabled(false), pc);
            return UserMapper.INSTANCE.toDto(userResource);
        });
    }

    @PostMapping("/enable")
    @ResponseStatus(HttpStatus.OK)
    @AuthorizedContext({ AuthContext.class })
    @AccessPermission(privilege = CorePrivilege.class,
            id = CorePrivilegeConsts.USER_MANAGEMENT_ID,
            ops = { AccessOp.WRITE })
    public CompletableFuture<UserDto> enableUser(
            @RequestParam("id") Long id
    ) {
        return queryPool.executeAndReturnFuture(pc -> {
            UserResource userResource = userService.update(id, new UserBuilder().withIsEnabled(true), pc);
            return UserMapper.INSTANCE.toDto(userResource);
        });
    }
}
