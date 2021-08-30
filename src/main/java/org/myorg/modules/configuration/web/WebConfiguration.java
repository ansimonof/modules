package org.myorg.modules.configuration.web;

import org.myorg.modules.web.argresolver.ContextHandlerMethodArgumentResolver;
import org.myorg.modules.web.auth.AuthService;
import org.myorg.modules.web.auth.context.Context;
import org.myorg.modules.web.converter.AccessOpConverter;
import org.myorg.modules.web.converter.PrivilegeEnumConverter;
import org.myorg.modules.web.interceptor.AuthorizingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
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
    @Autowired
    private ContextHandlerMethodArgumentResolver contextHandlerMethodArgumentResolver;


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(contextHandlerMethodArgumentResolver);
    }

    // Auth
    @Autowired
    private AuthService authService;

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
        return authService.auth();
    }

    // Converter
    @Autowired
    private PrivilegeEnumConverter privilegeEnumConverter;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(privilegeEnumConverter);
        registry.addConverter(new AccessOpConverter());
    }
}
