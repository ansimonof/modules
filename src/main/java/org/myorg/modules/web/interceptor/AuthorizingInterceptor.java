package org.myorg.modules.web.interceptor;

import org.myorg.modules.access.AccessOp;
import org.myorg.modules.access.PrivilegeAccessOpPair;
import org.myorg.modules.access.PrivilegeEnum;
import org.myorg.modules.exception.ModuleException;
import org.myorg.modules.util.ContextUtils;
import org.myorg.modules.web.ControllerInfo;
import org.myorg.modules.web.auth.context.AuthContext;
import org.myorg.modules.web.auth.context.Context;
import org.myorg.modules.web.auth.context.UnauthContext;
import org.myorg.modules.web.auth.context.source.UserSource;
import org.myorg.modules.web.ControllerInfoGetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class AuthorizingInterceptor implements HandlerInterceptor {

    @Autowired
    private Context<?> context;

    @Autowired
    private ControllerInfoGetter controllerInfoGetter;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        Map<Method, ControllerInfo> methodToControllerInfo = controllerInfoGetter.getMethodToControllerInfo();

        Method method = ((HandlerMethod) handler).getMethod();
        ControllerInfo controllerInfo = methodToControllerInfo.get(method);

        if (controllerInfo == null) {
            // Странно, мы ничего не знаем об этом методе
            return false;
        }

        Context<?> authContext = ContextUtils.copy(context);
        checkAuthContext(authContext, controllerInfo.getAuthContexts());
        checkAccessPermission(authContext, controllerInfo);

        return true;
    }


    private void checkAuthContext(Context<?> authContext,
                                  Set<Class<? extends UnauthContext>> authContexts) throws ModuleException {
        if (authContexts.contains(UnauthContext.class) && authContext instanceof AuthContext) {
            throw new ModuleException("Authorized context cannot executed unauthorized controller");
        }

        authContexts.stream()
                .filter(ac -> ac.isAssignableFrom(context.getClazz()))
                .findAny()
                .orElseThrow(() -> new ModuleException("Bad authorized context"));
    }

    private void checkAccessPermission(Context<?> authContext, ControllerInfo controllerInfo) throws ModuleException {
        PrivilegeEnum privilegeEnum = controllerInfo.getPrivilegeEnum();
        AccessOp[] accessOps = controllerInfo.getAccessOps();

        if (privilegeEnum == null) {
            return;
        }

        // Проверка на админа
        if (authContext.getSource() instanceof UserSource
                && ((UserSource) authContext.getSource()).isAdmin()) {
            return;
        }

        List<PrivilegeAccessOpPair> privilegeAccessOpPairs = authContext.getSource().getPrivilegeAccessOpPairs();
        PrivilegeAccessOpPair privilegeAccessOpPair = privilegeAccessOpPairs.stream()
                .filter(ap -> Objects.equals(ap.getPrivilegeEnum().getUniqueKey(), privilegeEnum.getUniqueKey()))
                .findAny()
                .orElseThrow(() -> new ModuleException("Access denied"));

        if (!privilegeAccessOpPair.getAccessOpCollection().contains(accessOps)) {
            throw new ModuleException("Access denied");
        }
    }

}
