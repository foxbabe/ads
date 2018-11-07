package com.sztouyun.advertisingsystem.web;

import com.sztouyun.advertisingsystem.model.mongodb.WebLogInfo;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;
import com.sztouyun.advertisingsystem.utils.HttpUtils;
import com.sztouyun.advertisingsystem.utils.ObjectMapperUtils;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiOperation;
import lombok.experimental.var;
import org.apache.calcite.linq4j.Linq4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;

public class WebLogAspect {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private MongoTemplate mongoTemplate;

    ThreadLocal<WebLogInfo> webLogInfoThreadLocal = new ThreadLocal<>();

    @Pointcut("within(com.sztouyun.advertisingsystem.api..*) && @annotation(io.swagger.annotations.ApiOperation)")
    public void webLog(){}

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        if(!"POST".equals(request.getMethod().toUpperCase()))
            return;
        Object requestData =null;
        if(joinPoint.getArgs() != null && joinPoint.getArgs().length>0){
            requestData =Linq4j.asEnumerable(joinPoint.getArgs()).firstOrDefault(a->a.getClass().getName().startsWith("com.sztouyun.advertisingsystem"));
            if(requestData != null && requestData instanceof BasePageInfo)
                return;
        }
        WebLogInfo webLogInfo = new WebLogInfo();
        if(requestData != null){
            webLogInfo.setRequest(ObjectMapperUtils.toJsonString(requestData));
        }
        webLogInfo.setStartTime(new Date());
        String url = request.getRequestURL().toString();
        webLogInfo.setUrl(url);
        webLogInfo.setMethod(request.getMethod());
        webLogInfo.setType(HttpUtils.getRequestType(request));
        webLogInfo.setClientIp(HttpUtils.getCliectIp(request));
        webLogInfo.setClassMethod(joinPoint.getTarget().getClass().getName() + "." +joinPoint.getSignature().getName());
        var apiOperation = ((MethodSignature)joinPoint.getSignature()).getMethod().getAnnotation(ApiOperation.class);
        if(apiOperation !=null){
            webLogInfo.setOperationName(apiOperation.value());
        }

        try{
            var user= AuthenticationService.getUser();
            if(user !=null){
                webLogInfo.setUserId(user.getId());
                webLogInfo.setUserName(user.getNickname());
                webLogInfo.setRoleId(user.getRoleId());
            }
        }catch (Exception ex){
        }

        webLogInfoThreadLocal.set(webLogInfo);
    }

    @AfterReturning(pointcut = "webLog()")
    public void doAfterReturning() throws Throwable {
        WebLogInfo webLogInfo = webLogInfoThreadLocal.get();
        if(webLogInfo ==null)
            return;

        webLogInfo.setEndTime(new Date());
        webLogInfo.setTimeConsuming(webLogInfo.getEndTime().getTime() - webLogInfo.getStartTime().getTime());
        webLogInfo.setSuccess(true);
        saveWebLogInfo(webLogInfo);
    }

    @AfterThrowing(pointcut = "webLog()", throwing="e")
    public  void doAfterThrowing(Throwable e) {
        WebLogInfo webLogInfo = webLogInfoThreadLocal.get();
        webLogInfo.setEndTime(new Date());
        webLogInfo.setTimeConsuming(webLogInfo.getEndTime().getTime() - webLogInfo.getStartTime().getTime());
        webLogInfo.setErrorMessage(e.getMessage()+ Arrays.toString(e.getStackTrace()));
        webLogInfo.setSuccess(false);
        saveWebLogInfo(webLogInfo);
    }

    private void saveWebLogInfo(WebLogInfo webLogInfo){
        try{
            mongoTemplate.insert(webLogInfo);
        }catch (Exception e){
            logger.error("保存操作日志失败",e);
        }
    }
}
