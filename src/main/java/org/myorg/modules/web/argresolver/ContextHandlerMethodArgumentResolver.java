package org.myorg.modules.web.argresolver;

import org.myorg.modules.util.ContextUtils;
import org.myorg.modules.web.auth.context.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class ContextHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    // В контроллер передавать Context нельзя, так его нельзя будет использовать
    // в другом потоке. Поэтому его нужно создать заново (ЭТО PROXY!!!)
    @Autowired
    private Context<?> context;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Context.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        return ContextUtils.copy(context);
    }
}
