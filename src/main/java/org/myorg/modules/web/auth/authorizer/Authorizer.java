package org.myorg.modules.web.auth.authorizer;

import org.myorg.modules.web.auth.context.UnauthContext;
import org.myorg.modules.web.auth.context.source.Source;

import javax.servlet.http.HttpServletRequest;

public interface Authorizer {

    UnauthContext<? extends Source> auth(HttpServletRequest request);

    boolean supports(HttpServletRequest request);
}
