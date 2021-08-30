package org.myorg.modules.module.core.controller.user.access;

import org.myorg.modules.access.AccessOp;
import org.myorg.modules.access.PrivilegeAccessOpPair;
import org.myorg.modules.access.PrivilegeEnum;
import org.myorg.modules.access.annotation.AccessPermission;
import org.myorg.modules.access.dto.PrivilegeDto;
import org.myorg.modules.access.service.AccessPrivilegeService;
import org.myorg.modules.exception.ModuleExceptionBuilder;
import org.myorg.modules.module.core.access.CorePrivilege;
import org.myorg.modules.module.core.access.CorePrivilegeConsts;
import org.myorg.modules.querypool.QueryPool;
import org.myorg.modules.web.annotation.AuthorizedContext;
import org.myorg.modules.web.auth.context.AuthContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@RestController
@RequestMapping("/core/access")
public class AccessPrivilegesController {

    @Autowired
    private QueryPool queryPool;

    @Autowired
    private AccessPrivilegeService accessPrivilegeService;

    @GetMapping("/privileges")
    @ResponseStatus(HttpStatus.OK)
    @AuthorizedContext({ AuthContext.class })
    @AccessPermission(privilege = CorePrivilege.class,
            id = CorePrivilegeConsts.USER_MANAGEMENT_ID,
            ops = { AccessOp.READ })
    public CompletableFuture<List<PrivilegeDto>> privileges() {
        return queryPool.executeAndReturnFuture(pc -> {
            Set<PrivilegeEnum> privileges = accessPrivilegeService.getAllPrivileges();

            List<PrivilegeDto> privilegeDtos = new ArrayList<>();
            for (PrivilegeEnum privilege : privileges) {
                PrivilegeDto privilegeDto = new PrivilegeDto();
                privilegeDto.setUniqueKey(privilege.getUniqueKey());

                AccessOp[] accessOps = Stream.of(AccessOp.values())
                        .filter(accessOp -> privilege.getAvailableOps().contains(accessOp))
                        .toArray(AccessOp[]::new);
                privilegeDto.setAccessOps(accessOps);

                privilegeDtos.add(privilegeDto);
            }

            return privilegeDtos;
        });
    }

    @GetMapping("/privileges/user")
    @ResponseStatus(HttpStatus.OK)
    @AuthorizedContext({ AuthContext.class })
    @AccessPermission(privilege = CorePrivilege.class,
            id = CorePrivilegeConsts.USER_MANAGEMENT_ID,
            ops = { AccessOp.READ })
    public CompletableFuture<List<PrivilegeDto>> userPrivileges(
            @RequestParam("user_id") Long userId
    ) {
        return queryPool.executeAndReturnFuture(pc -> {
            List<PrivilegeDto> privilegeDtos = new ArrayList<>();

            List<PrivilegeAccessOpPair> userPrivileges = accessPrivilegeService.getUserPrivileges(userId, pc);
            for (PrivilegeAccessOpPair userPrivilege : userPrivileges) {
                PrivilegeDto privilegeDto = new PrivilegeDto();
                privilegeDto.setUniqueKey(userPrivilege.getPrivilegeEnum().getUniqueKey());
                AccessOp[] accessOps = Stream.of(AccessOp.values())
                        .filter(accessOp -> userPrivilege.getAccessOpCollection().contains(accessOp))
                        .toArray(AccessOp[]::new);
                privilegeDto.setAccessOps(accessOps);
                privilegeDtos.add(privilegeDto);
            }

            return privilegeDtos;
        });
    }

    @PostMapping("/privileges/user")
    @ResponseStatus(HttpStatus.OK)
    @AuthorizedContext({ AuthContext.class })
    @AccessPermission(privilege = CorePrivilege.class,
            id = CorePrivilegeConsts.USER_MANAGEMENT_ID,
            ops = { AccessOp.WRITE })
    public CompletableFuture<Void> setPrivileges(
            @RequestParam("user_id") Long userId,
            @RequestParam("privilege") PrivilegeEnum privilegeEnum,
            @RequestParam("op") Set<AccessOp> accessOps
    ) {
        return queryPool.executeAndReturnFuture(pc -> {
            if (privilegeEnum == null) {
                throw ModuleExceptionBuilder.buildNoSuchPrivilegeException();
            }

            if (accessOps.contains(null)) {
                throw ModuleExceptionBuilder.buildAccessOpCannotBeNullException();
            }

            accessPrivilegeService.updatePrivilegesForUser(userId, privilegeEnum, accessOps.toArray(new AccessOp[0]), pc);
            return null;
        });
    }

}
