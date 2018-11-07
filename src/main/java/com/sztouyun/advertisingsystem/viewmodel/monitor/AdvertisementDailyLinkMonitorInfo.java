package com.sztouyun.advertisingsystem.viewmodel.monitor;

import lombok.Data;

/**
 * Created by szty on 2018/7/3.
 */
@Data
public class AdvertisementDailyLinkMonitorInfo {
    private String materialUrlStepId;

    private String storeId;


    private Integer action;


    private Integer triggerTimes;

}
