package com.ahdark.code.link.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {
    @Autowired
    RootApiPermissionChecker rootApiPermissionChecker;

    @Autowired
    RequestLogger requestLogger;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rootApiPermissionChecker).addPathPatterns("/api/root/**");
        registry.addInterceptor(requestLogger).addPathPatterns("/api/**");
    }
}
