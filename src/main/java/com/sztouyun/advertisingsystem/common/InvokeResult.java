package com.sztouyun.advertisingsystem.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "接口返回统一对象")
public class InvokeResult<T>{
    @ApiModelProperty(value = "状态码")
    private int code = 1;
    @ApiModelProperty(value = "错误信息")
    private String message;
    @ApiModelProperty(value = "成功时返回的对象")
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {

        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccessed() {
        return code == 1;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static InvokeResult SuccessResult(){
        return new InvokeResult();
    }

    public static <T> InvokeResult<T> SuccessResult(T data){
        InvokeResult<T> result = new InvokeResult<>();
        result.setData(data);
        return result;
    }

    public static <T> InvokeResult Fail(String message,int code,T data){
        InvokeResult result =new InvokeResult<T>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static InvokeResult Fail(String message){
        return Fail(message,-1,null);
    }

    public static InvokeResult Fail(String message,int code){
        return Fail(message,code,null);
    }
}
