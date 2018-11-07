package com.sztouyun.advertisingsystem.web.security;

import com.sztouyun.advertisingsystem.service.account.UserService;
import com.sztouyun.advertisingsystem.viewmodel.account.UserViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Riber on 2017/7/29 0029.
 */
@Component
public class MockUserAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

    @Value("${mock.username}")
    private String mockUsername;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String username = request.getParameter("mockUser");
        if (StringUtils.isEmpty(username)) {
            username = mockUsername;
        }
        if (!StringUtils.isEmpty(username)) {
            UserViewModel user = userService.getUserAndRoleFromCache(username);
            if (user != null) {
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            }
        }
        filterChain.doFilter(request, response);
    }

}
