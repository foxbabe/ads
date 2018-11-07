package com.sztouyun.advertisingsystem.config;

import com.sztouyun.advertisingsystem.common.jpa.JoinFetchRepositoryFactoryBean;
import com.sztouyun.advertisingsystem.viewmodel.account.UserViewModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.context.SecurityContextHolder;

@EnableJpaAuditing
@Configuration
@EnableJpaRepositories(value = "com.sztouyun.advertisingsystem.repository",
        repositoryFactoryBeanClass = JoinFetchRepositoryFactoryBean.class)
public class JpaConfig {
    @Bean
    public AuditorAware<String> createAuditorProvider() {
        return new SecurityAuditor();
    }

    public static class SecurityAuditor implements AuditorAware<String> {
        @Override
        public String getCurrentAuditor() {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(!(principal instanceof UserViewModel)) {
                return null;
            }
            UserViewModel user = (UserViewModel) principal;
            return user.getId();
        }
    }
}
