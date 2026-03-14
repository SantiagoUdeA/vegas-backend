package com.vegas.sistema_gestion_operativa.common.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final HibernateFilterInterceptor hibernateFilterInterceptor;

    @Autowired
    public WebConfig(HibernateFilterInterceptor hibernateFilterInterceptor) {
        this.hibernateFilterInterceptor = hibernateFilterInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(hibernateFilterInterceptor);
    }
}
