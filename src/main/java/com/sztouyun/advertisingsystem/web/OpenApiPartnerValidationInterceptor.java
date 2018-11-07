package com.sztouyun.advertisingsystem.web;


import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.model.partner.CooperationPartner;
import com.sztouyun.advertisingsystem.service.partner.CooperationPartnerService;
import com.sztouyun.advertisingsystem.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by RiberLi on 2018/1/26 0026.
 */
@Component
public class OpenApiPartnerValidationInterceptor implements HandlerInterceptor {
    @Autowired
    private CooperationPartnerService cooperationPartnerService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String partnerId = request.getHeader("partnerId");
        if(StringUtils.isEmpty(partnerId)){
            HttpUtils.writeValueToResponse(response, InvokeResult.Fail("参数错误", 500));
            return false;
        }
        CooperationPartner partner = cooperationPartnerService.findCooperationPartnerById(partnerId);
        if(partner ==null){
            HttpUtils.writeValueToResponse(response, InvokeResult.Fail("合作方不存在", Constant.NO_PERMISSION));
            return false;
        }
        if(partner.isDisabled()){
            HttpUtils.writeValueToResponse(response, InvokeResult.Fail("合作方已禁用", Constant.NO_PERMISSION));
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
