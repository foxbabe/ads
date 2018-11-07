package com.sztouyun.advertisingsystem.web;


import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by RiberLi on 2018/1/26 0026.
 */
@Component
public class OpenApiAuthorizationInterceptor implements HandlerInterceptor {
    @Value("${openapi.header.key}")
    private String openApiHeadKey;

    @Value("${openapi.header.value}")
    private String openApiHeadValue;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        if(!openApiHeadValue.equals(request.getHeader(openApiHeadKey))){
            HttpUtils.writeValueToResponse(response, InvokeResult.Fail("权限不足", Constant.NO_PERMISSION));
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
