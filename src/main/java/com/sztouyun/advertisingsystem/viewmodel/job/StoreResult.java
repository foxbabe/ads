package com.sztouyun.advertisingsystem.viewmodel.job;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StoreResult<T> {
    @JsonProperty("status")
    private int status;
    @JsonProperty("msg")
    private String msg;
    @JsonProperty("data")
    private List<T> data = new ArrayList<>();
    public boolean isSuccessful(){
        return status ==0;
    }
}
