package com.sztouyun.advertisingsystem.exception;


public class BusinessException extends  RuntimeException {

    private String message;
    private int code;

    public BusinessException(String message){
        this(message,-1);
    }

    public BusinessException(String message ,int code){
        super(message);
        this.message =message;
        this.code =code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
