package com.sztouyun.advertisingsystem.config;

import com.sztouyun.advertisingsystem.web.OpenApiAuthorizationInterceptor;
import com.sztouyun.advertisingsystem.web.OpenApiPartnerValidationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class OpenApiInterceptorConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private EnvironmentConfig environmentConfig;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if(!environmentConfig.isDebug()){
            //授权验证
            registry.addInterceptor(getOpenApiAuthorizationInterceptor()).addPathPatterns("/open/api/**");
        }
        if(!environmentConfig.isLocal()){
            //registry.addInterceptor(getOpenApiPartnerValidationInterceptor()).addPathPatterns("/open/api/**");
        }
    }


    @Bean
    public HandlerInterceptor getOpenApiAuthorizationInterceptor(){
        return new OpenApiAuthorizationInterceptor();
    }

    @Bean
    public HandlerInterceptor getOpenApiPartnerValidationInterceptor(){
        return new OpenApiPartnerValidationInterceptor();
    }
}
