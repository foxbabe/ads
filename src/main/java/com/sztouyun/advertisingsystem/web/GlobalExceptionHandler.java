package com.sztouyun.advertisingsystem.web;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.config.EnvironmentConfig;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;


@ControllerAdvice
class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EnvironmentConfig environmentConfig;

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Object defaultErrorHandler(HttpServletRequest request, Exception e) throws Exception {
        logger.error("URL:"+request.getRequestURI(),e.getMessage(),e);
        boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
        boolean isApi =request.getRequestURL().toString().contains("/api/") || request.getRequestURL().toString().contains("/open/api/") || request.getRequestURL().toString().contains("/internal/api/");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("系统异常请稍后重试！");
        if(!environmentConfig.isOnline()) {
            stringBuilder.append(e.getMessage() + "\r\n");
            StackTraceElement[] trace = e.getStackTrace();
            for (StackTraceElement s : trace) {
                stringBuilder.append("\tat " + s + "\r\n");
            }
        }
        String errorMessage = stringBuilder.toString();
        if(isAjax || isApi){
            return InvokeResult.Fail(errorMessage.replace("\r\n", "\\r\\n").replace("\t", "\\t"),500);
        }else {
            return  new RedirectView("/#/login");
        }
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseBody
    public Object httpMessageNotReadableExceptionHandler(HttpServletRequest request, Exception e) throws Exception {
        logger.error("URL:"+request.getRequestURI(),e.getMessage(),e);
        return InvokeResult.Fail("参数类型错误！", 100);
    }

    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public Object businessExceptionHandler(HttpServletRequest request, Exception e) throws Exception {
        BusinessException exception =(BusinessException)e;
        return InvokeResult.Fail(exception.getMessage(),exception.getCode());
    }

    @ExceptionHandler(value = ValidationException.class)
    @ResponseBody
    public Object validationExceptionHandler(HttpServletRequest request, Exception e) throws Exception {
        if(e.getCause() instanceof  BusinessException)
        {
            BusinessException exception =(BusinessException)e.getCause();
            return InvokeResult.Fail(exception.getMessage(),exception.getCode());
        }
        return InvokeResult.Fail("参数错误");
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseBody
    public Object accessDeniedExceptionHandler(HttpServletRequest request, Exception e) throws Exception {
        return InvokeResult.Fail("权限不足", Constant.NO_PERMISSION);
    }
}
