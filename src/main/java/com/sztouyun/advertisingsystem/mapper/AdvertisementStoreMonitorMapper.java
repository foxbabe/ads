package com.sztouyun.advertisingsystem.mapper;

import com.sztouyun.advertisingsystem.model.monitor.AdvertisementPositionDailyDisplayTimesStatistic;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.AdvertisementInfoModel;
import com.sztouyun.advertisingsystem.viewmodel.monitor.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import java.util.Date;
import java.util.List;

@Mapper
@CacheConfig(cacheNames = "AdvertisementInfo")
public interface AdvertisementStoreMonitorMapper {
    void saveAdvertisementDailyDeliveryMonitorInfo(AdvertisementDailyDeliveryMonitor advertisementDailyDeliveryMonitor);

    @Cacheable(key = "#p0",condition="#p0!=null")
    AdvertisementInfoModel getAdvertisementInfo(String advertisementMaterialId);

    void updateAdvertisementMonitorIfo(List<DeliveryMonitorStatistic> info);

    void reCalcAdvertisementDisplayTimes(List<String> advertisementIds);

    List<AdvertisementStoreMonitorItem> getMonitorStoreItems(AdvertisementStoreMonitorItemRequest request);

    long getMonitorStoreItemCount(AdvertisementStoreMonitorItemRequest request);

	void updateAdvertisingDailyStoreMonitorStatistic(Date date);

    List<AdvertisementPositionDailyDisplayTimesStatistic> getAdvertisementPositionDailyDisplayTimesStatistic(DisplayTimesBrokenChartRequest displayTimesBrokenChartRequest);
}
