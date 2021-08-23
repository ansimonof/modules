package org.myorg.modules.configuration.web;

import org.myorg.modules.exception.ModuleException;
import org.myorg.modules.web.argresolver.ContextHandlerMethodArgumentResolver;
import org.myorg.modules.web.auth.AuthService;
import org.myorg.modules.web.auth.authorizer.UserSessionAuthorizer;
import org.myorg.modules.web.auth.context.Context;
import org.myorg.modules.web.interceptor.AuthorizingInterceptor;
import org.myorg.modules.web.session.SessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebMvc
@ComponentScan("org.myorg")
public class WebConfiguration implements WebMvcConfigurer {

    // ArgumentResolvers

    @Bean
    ContextHandlerMethodArgumentResolver contextHandlerMethodArgumentResolver() {
        return new ContextHandlerMethodArgumentResolver();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(contextHandlerMethodArgumentResolver());
    }

    // Auth

    @Bean
    AuthService authService() {
        return new AuthService();
    }

    @Bean
    AuthorizingInterceptor authorizingInterceptor() {
        return new AuthorizingInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizingInterceptor());
    }

    @Bean
    @RequestScope
    Context<?> getAuthContext() throws Exception {
        return authService().auth();
    }

    @Bean
    SessionManager sessionManager() {
        return new SessionManager();
    }


}
