package com.sztouyun.advertisingsystem.viewmodel.job;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class StoreDailyOrderDay {

    @JsonProperty("Value")
    private Date date;

}
