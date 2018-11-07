package com.sztouyun.advertisingsystem.scheduled;

import com.sztouyun.advertisingsystem.mapper.AdvertisementStoreMonitorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;

/**
 * Created by szty on 2018/5/30.
 */
@Component
public class AdvertisementDailyStoreMonitorStatisticTask extends BaseScheduledTask {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private AdvertisementStoreMonitorMapper advertisementStoreMonitorMapper;

    @Scheduled(cron = "${advertisement.daily.store.monitor.statistic.time}")
    public void  updateAdvertisingDailyStoreMonitorStatistic(){
        Date date= new org.joda.time.LocalDate().minusDays(1).toDateTimeAtStartOfDay().toDate();
        advertisementStoreMonitorMapper.updateAdvertisingDailyStoreMonitorStatistic(date);
    }
}