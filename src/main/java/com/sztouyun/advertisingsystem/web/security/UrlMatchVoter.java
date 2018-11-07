package com.sztouyun.advertisingsystem.web.security;

import com.sztouyun.advertisingsystem.model.account.User;
import com.sztouyun.advertisingsystem.service.account.RoleService;
import com.sztouyun.advertisingsystem.service.system.PermissionService;
import org.apache.calcite.linq4j.Linq4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class UrlMatchVoter implements AccessDecisionVoter<Object> {
    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RoleService roleService;

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return attribute instanceof UrlConfigAttribute;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
        if(authentication == null || authentication.getPrincipal() == null || !(authentication.getPrincipal() instanceof User))
            return ACCESS_GRANTED;

        if(attributes==null || attributes.isEmpty())
            return ACCESS_DENIED;
        UrlConfigAttribute urlConfigAttribute =(UrlConfigAttribute) Linq4j.asEnumerable(attributes).firstOrDefault(attribute->(attribute instanceof UrlConfigAttribute));
        if(urlConfigAttribute == null)
            return ACCESS_DENIED;
        if(!urlConfigAttribute.getHttpServletRequest().getRequestURI().startsWith("/api/"))
            return ACCESS_GRANTED;
        User user =(User) authentication.getPrincipal();
        if(user != null && user.isAdmin())
            return ACCESS_GRANTED;

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        List<String> authenticatedUrls = new ArrayList<>();
        for (GrantedAuthority authority : authorities) {
            if (!(authority instanceof UrlGrantedAuthority))
                continue;
            UrlGrantedAuthority urlGrantedAuthority = (UrlGrantedAuthority) authority;
            if (StringUtils.isBlank(urlGrantedAuthority.getAuthority()))
                continue;
            authenticatedUrls.add(urlGrantedAuthority.getAuthority());
            //用Spring已经实现的AntPathRequestMatcher进行匹配，这样我们数据库中的url也就支持ant风格的配置了（例如：/xxx/user/**）
            AntPathRequestMatcher antPathRequestMatcher = new AntPathRequestMatcher(urlGrantedAuthority.getAuthority(), urlConfigAttribute.getHttpServletRequest().getMethod());
            if (antPathRequestMatcher.matches(urlConfigAttribute.getHttpServletRequest()))
                return ACCESS_GRANTED;
        }

        List<String> allApiUrls = permissionService.getAllPermissionApiUrlsFromCache();
        for (String apiUrl:allApiUrls) {
            if(authenticatedUrls.contains(apiUrl))
                continue;
            //匹配上未授权的url，直接拒绝访问
            AntPathRequestMatcher antPathRequestMatcher = new AntPathRequestMatcher(apiUrl, urlConfigAttribute.getHttpServletRequest().getMethod());
            if(antPathRequestMatcher.matches(urlConfigAttribute.getHttpServletRequest())){
                if(apiUrl.toLowerCase().endsWith("id}")){
                  String requestUrl = urlConfigAttribute.getHttpServletRequest().getRequestURI();
                  if(requestUrl.length() - requestUrl.lastIndexOf("/") != 33)
                      continue;
                }
                return  ACCESS_DENIED;
            }
        }
        return ACCESS_GRANTED;
    }
}
