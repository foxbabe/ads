package com.sztouyun.advertisingsystem.viewmodel.monitor;

import lombok.Data;

import java.util.Date;

/**
 * Created by szty on 2018/7/3.
 */
@Data
public class AdvertisementDailyLinkMonitorStatistic extends AdvertisementDailyLinkMonitorInfo {

    private Date date;

    private Integer advertisementPositionCategory;

    private Integer linkType;

    private Integer stepType;

    private Date createdTime;

    private Date updatedTime;

    public Date getCreatedTime() {
        return new Date();
    }

    public Date getUpdatedTime() {
        return new Date();
    }
}
