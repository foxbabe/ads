package com.sztouyun.advertisingsystem.viewmodel.job;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class StoreDailyOrderCountViewModel implements Serializable {
    @JsonProperty("storeno")
    private String storeNo;

    @JsonProperty("orderqty")
    private Integer count;

    @JsonProperty("orderday")
    private StoreDailyOrderDay orderDay;
}
