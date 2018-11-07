package com.sztouyun.advertisingsystem.viewmodel.job;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StoreDailyOrderCountRequest {

    @JsonProperty("StartDate")
    private String startDate;

    @JsonProperty("EndDate")
    private String endDate;

    @JsonProperty("StoreNos")
    private String storeNumbers;

    public StoreDailyOrderCountRequest() {
    }

    public StoreDailyOrderCountRequest(String startDate, String endDate, String storeNumbers) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.storeNumbers = storeNumbers;
    }
}
