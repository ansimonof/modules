package org.myorg.modules.web;

import org.myorg.modules.access.AccessOp;
import org.myorg.modules.access.PrivilegeEnum;
import org.myorg.modules.access.annotation.AccessPermission;
import org.myorg.modules.access.annotation.AuthorizedContext;
import org.myorg.modules.web.ControllerInfo;
import org.myorg.modules.web.auth.context.UnauthContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ControllerInfoGetter {

    private final Map<Method, ControllerInfo> methodToControllerInfo = new HashMap<>();

    @Autowired
    // Инжектить по конструктору нельзя, так как здесь цикличекая зависимость
    private RequestMappingHandlerMapping handlerMapping;

    @PostConstruct
    private void init() {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
        for (HandlerMethod handlerMethod : handlerMethods.values()) {
            Method method = handlerMethod.getMethod();
            ControllerInfo controllerInfo = new ControllerInfo();

            if (method.isAnnotationPresent(AccessPermission.class)) {
                AccessPermission accessPermissionAnn = method.getAnnotation(AccessPermission.class);
                int id = accessPermissionAnn.id();
                AccessOp[] ops = accessPermissionAnn.ops();
                Class<? extends PrivilegeEnum> privilege = accessPermissionAnn.privilege();

                PrivilegeEnum accessEnum = null;
                for (PrivilegeEnum enumConstant : privilege.getEnumConstants()) {
                    if (enumConstant.intValue() == id) {
                        accessEnum = enumConstant;
                        break;
                    }
                }

                if (accessEnum == null) {
                    throw new RuntimeException("No privilege enum(" + privilege + ") with such id=" + id);
                }

                controllerInfo.setPrivilegeEnum(accessEnum);
                controllerInfo.setAccessOps(ops);
            }

            Set<Class<? extends UnauthContext>> authContexts = new HashSet<>();
            if (method.isAnnotationPresent(AuthorizedContext.class)) {
                AuthorizedContext authorizeAnn = method.getAnnotation(AuthorizedContext.class);
                authContexts.addAll(reduceAuthContextClasses(authorizeAnn.value()));
            } else {
                authContexts.add(UnauthContext.class);
            }

            controllerInfo.setAuthContexts(authContexts);

            methodToControllerInfo.put(method, controllerInfo);
        }
    }

    private Set<Class<? extends UnauthContext>> reduceAuthContextClasses(Class<? extends UnauthContext>[] authClasses) {
        Set<Class<? extends UnauthContext>> unUsableClasses = new HashSet<>();
        for (Class<? extends UnauthContext> outerClass : authClasses) {
            for (Class<? extends UnauthContext> innerClass : authClasses) {
                if (outerClass != innerClass) {
                    if (innerClass.isAssignableFrom(outerClass)) {
                        unUsableClasses.add(innerClass);
                    }
                }
            }
        }
        return Arrays.stream(authClasses)
                .filter(clazz -> !unUsableClasses.contains(clazz))
                .collect(Collectors.toSet());
    }

    public Map<Method, ControllerInfo> getMethodToControllerInfo() {
        return methodToControllerInfo;
    }
}
