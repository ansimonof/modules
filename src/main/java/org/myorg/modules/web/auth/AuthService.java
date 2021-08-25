package org.myorg.modules.web.auth;

import org.myorg.modules.exception.ModuleException;
import org.myorg.modules.exception.ModuleExceptionBuilder;
import org.myorg.modules.web.auth.authorizer.Authorizer;
import org.myorg.modules.web.auth.context.UnauthContext;
import org.myorg.modules.web.auth.context.source.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
public class AuthService {

    @Autowired
    private HttpServletRequest request;

    private final List<? extends Authorizer> authorizers;

    @Autowired
    public AuthService(List<? extends Authorizer> authorizers) {
        this.authorizers = authorizers;
    }

    public UnauthContext<? extends Source> auth() throws Exception {
        Authorizer authorizer = getAuthorizer();

        // Нет авторизатора, который хотел бы отработать
        if (authorizer == null) {
            return new UnauthContext<>(new Source());
        }

        UnauthContext<? extends Source> context = authorizer.auth(request);
        // Некорректные credentials
        if (context == null) {
            throw ModuleExceptionBuilder.buildIncorrectCredentialsException();
        }

        return context;
    }

    private Authorizer getAuthorizer() throws ModuleException {
        Authorizer authorizer = null;
        for (Authorizer auth : authorizers) {
            if (auth.supports(request)) {
                if (authorizer != null) {
                    throw ModuleExceptionBuilder.buildAmbiguityAuthException();
                }
                authorizer = auth;
            }
        }
        return authorizer;
    }
}
