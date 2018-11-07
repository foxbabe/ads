package com.sztouyun.advertisingsystem.model.mongodb;

import lombok.Data;

import java.util.Date;

@Data
public class WebLogInfo {
    //客户端请求ip
    private String clientIp;
    //客户端请求路径
    private String url;
    //操作名称
    private String operationName;
    //终端请求方式,普通请求,ajax请求
    private String type;
    //请求方式method,post,get等
    private String method;
    //请求的类及方法
    private String classMethod;
    //post请求的参数
    private String request;
    //接口异常信息
    private String errorMessage;
    //请求耗时单位
    private long timeConsuming;
    //异常描述
    private String exceptionMessage;
    //请求开始时间
    private Date startTime;
    //请求结束时间
    private Date endTime;
    //是否成功
    private boolean success;

    private String userId;
    private String userName;
    private String RoleId;
}
