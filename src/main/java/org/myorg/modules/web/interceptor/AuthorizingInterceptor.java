package org.myorg.modules.web.interceptor;

import org.myorg.modules.access.AccessOp;
import org.myorg.modules.access.PrivilegeAccessOpPair;
import org.myorg.modules.access.PrivilegeEnum;
import org.myorg.modules.exception.ModuleException;
import org.myorg.modules.exception.ModuleExceptionBuilder;
import org.myorg.modules.util.ContextUtils;
import org.myorg.modules.web.ControllerInfo;
import org.myorg.modules.web.ControllerInfoGetter;
import org.myorg.modules.web.auth.context.AuthContext;
import org.myorg.modules.web.auth.context.Context;
import org.myorg.modules.web.auth.context.UnauthContext;
import org.myorg.modules.web.auth.context.source.UserSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Component
public class AuthorizingInterceptor implements AsyncHandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(AuthorizingInterceptor.class);

    @Autowired
    private Context<?> proxyContext;

    @Autowired
    private ControllerInfoGetter controllerInfoGetter;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        //log.info("HttpRequest (method: {}): {}",request.getMethod(), request.getRequestURI());

        Map<Method, ControllerInfo> methodToControllerInfo = controllerInfoGetter.getMethodToControllerInfo();

        Method method = ((HandlerMethod) handler).getMethod();
        ControllerInfo controllerInfo = methodToControllerInfo.get(method);
        if (controllerInfo == null) {
            // Странно, мы ничего не знаем об этом методе
            log.warn("Unknown method: {} ", method.getName());
            return false;
        }

        Context<?> authContext = ContextUtils.copy(proxyContext);
        checkAuthContext(authContext, controllerInfo.getAuthContexts());
        checkAccessPermission(authContext, controllerInfo);

        return true;
    }

    private void checkAuthContext(Context<?> authContext,
                                  Set<Class<? extends UnauthContext>> authContexts) throws ModuleException {
        if (authContexts.contains(UnauthContext.class) && authContext instanceof AuthContext) {
            throw ModuleExceptionBuilder.buildBadAuthContextException(UnauthContext.class, AuthContext.class);
        }

        authContexts.stream()
                .filter(ac -> ac.isAssignableFrom(proxyContext.getClazz()))
                .findAny()
                .orElseThrow(ModuleExceptionBuilder::buildAccessDeniedException);
    }

    private void checkAccessPermission(Context<?> authContext, ControllerInfo controllerInfo) throws ModuleException {
        PrivilegeEnum privilegeEnum = controllerInfo.getPrivilegeEnum();
        AccessOp[] accessOps = controllerInfo.getAccessOps();

        if (privilegeEnum == null) {
            return;
        }

        if (authContext.getSource() instanceof UserSource) {
            UserSource userSource = (UserSource) authContext.getSource();

            // Проверка на админа
            if (userSource.isAdmin()) {
                return;
            }

            // Проверка на бан
            if (!userSource.isEnabled()) {
                throw ModuleExceptionBuilder.buildUserIsBannedException();
            }
        }

        List<PrivilegeAccessOpPair> privilegeAccessOpPairs = authContext.getSource().getPrivilegeAccessOpPairs();
        PrivilegeAccessOpPair privilegeAccessOpPair = privilegeAccessOpPairs.stream()
                .filter(ap -> Objects.equals(ap.getPrivilegeEnum().getUniqueKey(), privilegeEnum.getUniqueKey()))
                .findAny()
                .orElseThrow(ModuleExceptionBuilder::buildAccessDeniedException);

        if (!privilegeAccessOpPair.getAccessOpCollection().contains(accessOps)) {
            throw ModuleExceptionBuilder.buildAccessDeniedException();
        }
    }

}
