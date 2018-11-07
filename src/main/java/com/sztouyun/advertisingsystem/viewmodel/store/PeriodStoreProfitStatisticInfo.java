package com.sztouyun.advertisingsystem.viewmodel.store;

import lombok.Data;

import java.util.Date;

@Data
public class PeriodStoreProfitStatisticInfo {
    private String storeId;
    private Date startDate;
    private Date endDate;
    private Double shareAmount;
    private Double settledAmount;
}
