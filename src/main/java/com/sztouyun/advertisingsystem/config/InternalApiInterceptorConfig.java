package com.sztouyun.advertisingsystem.config;

import com.sztouyun.advertisingsystem.web.InternalApiAuthorizationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class InternalApiInterceptorConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private EnvironmentConfig environmentConfig;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if(!environmentConfig.isDebug()){
            //授权验证
            registry.addInterceptor(getInternalApiAuthorizationInterceptor()).addPathPatterns("/internal/api/**");
        }

    }

    @Bean
    public HandlerInterceptor getInternalApiAuthorizationInterceptor(){
        return new InternalApiAuthorizationInterceptor();
    }
}
