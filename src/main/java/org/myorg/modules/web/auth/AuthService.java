package org.myorg.modules.web.auth;

import org.myorg.modules.exception.ModuleException;
import org.myorg.modules.web.auth.authorizer.Authorizer;
import org.myorg.modules.web.auth.context.UnauthContext;
import org.myorg.modules.web.auth.context.source.Source;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class AuthService {

    @Autowired
    private List<? extends Authorizer> authorizers;

    @Autowired
    private HttpServletRequest request;

    public UnauthContext<? extends Source> auth() throws Exception {
        Authorizer authorizer = getAuthorizer();

        // Нет авторизатора, который хотел бы отработать
        if (authorizer == null) {
            return new UnauthContext<>(new Source());
        }

        UnauthContext<? extends Source> context = authorizer.auth(request);
        // Некорректные credentials
        if (context == null) {
            throw new ModuleException("Bad auth");
        }

        return context;
    }

    private Authorizer getAuthorizer() throws ModuleException {
        Authorizer authorizer = null;
        for (Authorizer auth : authorizers) {
            if (auth.isSupport(request)) {
                if (authorizer != null) {
                    throw new ModuleException("Problem in auth");
                }
                authorizer = auth;
            }
        }
        return authorizer;
    }
}
