package com.sztouyun.advertisingsystem.viewmodel.job;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OmsResult<T> {
    @JsonProperty("StateCode")
    private int stateCode;
    @JsonProperty("Message")
    private String message;
    @JsonProperty("Data")
    private T data;

    public int getStateCode() {
        return stateCode;
    }

    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccessful(){
        return stateCode ==1;
    }
}
