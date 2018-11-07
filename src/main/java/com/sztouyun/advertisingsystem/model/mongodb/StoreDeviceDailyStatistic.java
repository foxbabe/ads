package com.sztouyun.advertisingsystem.model.mongodb;

import lombok.Data;


@Data
public class StoreDeviceDailyStatistic {
    private Long date;

    private String storeId;

    private Long openingTimeBegin = 0L;

    private Long openingTimeEnd = 0L;

    private Long openingTimeDuration = 0L;
}
