package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PeriodStoreProfitShareCalculateViewModel {
    private Date startDate;

    private Date endDate;

    private Double errorTolerantRate;

    private List<String> storeIds;

    public PeriodStoreProfitShareCalculateViewModel(Date startDate, Date endDate, Double errorTolerantRate, List<String> storeIds) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.errorTolerantRate = errorTolerantRate;
        this.storeIds = storeIds;
    }

    public PeriodStoreProfitShareCalculateViewModel(Date startDate, Date endDate, List<String> storeIds) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.storeIds = storeIds;
    }

    public PeriodStoreProfitShareCalculateViewModel() {
    }
}
