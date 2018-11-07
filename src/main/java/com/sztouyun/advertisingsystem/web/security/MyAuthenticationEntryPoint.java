package com.sztouyun.advertisingsystem.web.security;

import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.utils.HttpUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        boolean isAjax = "XMLHttpRequest".equals(httpServletRequest.getHeader("X-Requested-With"));
        boolean isApi =httpServletRequest.getRequestURL().toString().contains("/api/");
        if(isAjax || isApi) {
            HttpUtils.writeValueToResponse(httpServletResponse, InvokeResult.Fail("请先登录！", 401));
            return;
        }
        httpServletResponse.sendRedirect("#/login");
    }
}
