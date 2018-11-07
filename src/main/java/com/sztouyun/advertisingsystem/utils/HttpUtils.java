package com.sztouyun.advertisingsystem.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpUtils {
    public static void writeValueToResponse(HttpServletResponse httpServletResponse, Object value){
        Logger logger = LoggerFactory.getLogger("writeValueToResponse");
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setHeader("Content-type", "application/json;charset=utf-8");
        try {
            String message =new ObjectMapper().writeValueAsString(value);
            logger.info(message);
            httpServletResponse.getWriter().append(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        httpServletResponse.setStatus(200);
    }

    public static Map<String,Object> getCommonNewOmsRequestParam(Long startDate, Long endDate, Integer pageSize, Integer pageNum){
        Map<String,Object> param=new HashMap(){{
            put("startDate",startDate);
            put("endDate",endDate);
            put("pageSize",pageSize);
            put("pageNum",pageNum);
        }};
        return param;
    }
    public static Map<String,Object> getDefaultCommonParam(Long startDate,Long endDate){
        return getCommonNewOmsRequestParam(startDate,endDate,200,1);
    }

    public static void setHttpTimeout(RestTemplate restTemplate, int timeOut) {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectTimeout(timeOut);
        httpRequestFactory.setReadTimeout(timeOut);
        restTemplate.setRequestFactory(httpRequestFactory);
    }

    /**
     * 获取客户端ip地址
     */
    public static String getCliectIp(HttpServletRequest request)
    {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.trim() == "" || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.trim() == "" || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.trim() == "" || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // 多个路由时，取第一个非unknown的ip
        final String[] arr = ip.split(",");
        for (final String str : arr) {
            if (!"unknown".equalsIgnoreCase(str)) {
                ip = str;
                break;
            }
        }
        return ip;
    }

    /**
     * 判断是否为ajax请求
     */
    public static String getRequestType(HttpServletRequest request) {
        return request.getHeader("X-Requested-With");
    }

}
