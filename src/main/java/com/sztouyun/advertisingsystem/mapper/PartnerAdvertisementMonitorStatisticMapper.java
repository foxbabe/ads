package com.sztouyun.advertisingsystem.mapper;

import com.sztouyun.advertisingsystem.model.monitor.OrderDailyDeliveryMonitorStatistic;
import com.sztouyun.advertisingsystem.model.system.Area;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.AdvertisementInfoModel;
import com.sztouyun.advertisingsystem.viewmodel.monitor.OrderDailyMonitorDto;
import com.sztouyun.advertisingsystem.viewmodel.monitor.PartnerOrderStoreMonitorItem;
import com.sztouyun.advertisingsystem.viewmodel.monitor.PartnerOrderStoreMonitorRequest;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

@Mapper
@CacheConfig(cacheNames = "OrderDeliveryInfo")
public interface PartnerAdvertisementMonitorStatisticMapper {
    List<PartnerOrderStoreMonitorItem> getPartnerOrderStoreMonitorItems(PartnerOrderStoreMonitorRequest request);

    Long getPartnerOrderStoreMonitorItemCount(PartnerOrderStoreMonitorRequest request);

    List<Area> getValidAreaInPartnerStoreMonitor(String orderId);

    void saveOrderDailyDeliveryLog(List<OrderDailyDeliveryMonitorStatistic> orderDailyDeliveryMonitorStatistics);

    void updateOrderDailyStoreActiveCountAndDisplayTimes(OrderDailyMonitorDto param);

    void updatePartnerDeliveryMonitorInfo(List<String> orderIds);

    @Cacheable(key = "#p0",condition="#p0!=null")
    AdvertisementInfoModel getOrderAdvertisementInfo(String advertisementMaterialId);
}
