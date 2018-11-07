package com.sztouyun.advertisingsystem.web.security;

import com.google.common.collect.ImmutableList;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.config.EnvironmentConfig;
import com.sztouyun.advertisingsystem.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private MyUserDetailsService detailsService;

    @Autowired
    private MockUserAuthenticationFilter mockUserAuthenticationFilter;

    @Autowired
    private EnvironmentConfig environmentConfig;

    @Autowired
    private UrlMatchVoter urlMatchVoter;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().disable();
        http.addFilterBefore(filterSecurityInterceptor(authenticationManager(),accessDecisionManager(),securityMetadataSource()),FilterSecurityInterceptor.class)
                .exceptionHandling().accessDeniedHandler((httpServletRequest, httpServletResponse, e) ->
                    HttpUtils.writeValueToResponse(httpServletResponse,InvokeResult.Fail("权限不足", Constant.NO_PERMISSION)))
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/", "/api/user/resetPassword/**").permitAll()
                .antMatchers("/api/**").authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(new MyAuthenticationEntryPoint())
                .and()
                .cors()
                .and()
                .csrf()
                .disable();
        JwtAuthorizationTokenFilter authenticationTokenFilter = new JwtAuthorizationTokenFilter(userDetailsService(), jwtTokenUtil, tokenHeader);
        http.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        if (environmentConfig.isDebug()) {
            http.addFilterBefore(mockUserAuthenticationFilter, JwtAuthorizationTokenFilter.class);
        }
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(detailsService);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/static/**","/webjars/**","swagger-ui.html");
        super.configure(web);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(ImmutableList.of("*"));
        configuration.setAllowedMethods(ImmutableList.of("HEAD",
                "GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(ImmutableList.of("Authorization", "Cache-Control", "Content-Type","Content-Disposition"));
        configuration.addExposedHeader("Content-Disposition");
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AccessDecisionManager accessDecisionManager(){
        List<AccessDecisionVoter<? extends Object>> voters = new ArrayList<>();
        voters.add(urlMatchVoter);
        return new AffirmativeBased(voters);
    }

    @Bean
    FilterInvocationSecurityMetadataSource securityMetadataSource(){
        return new UrlFilterInvocationSecurityMetadataSource();
    }

    private UrlFilterSecurityInterceptor filterSecurityInterceptor(AuthenticationManager authenticationManager,
                                                               AccessDecisionManager accessDecisionManager,
                                                                  FilterInvocationSecurityMetadataSource securityMetadataSource){
        UrlFilterSecurityInterceptor urlFilterSecurityInterceptor = new UrlFilterSecurityInterceptor();
        urlFilterSecurityInterceptor.setAuthenticationManager(authenticationManager);
        urlFilterSecurityInterceptor.setAccessDecisionManager(accessDecisionManager);
        urlFilterSecurityInterceptor.setSecurityMetadataSource(securityMetadataSource);
        urlFilterSecurityInterceptor.setAlwaysReauthenticate(false);
        return urlFilterSecurityInterceptor;
    }
}
