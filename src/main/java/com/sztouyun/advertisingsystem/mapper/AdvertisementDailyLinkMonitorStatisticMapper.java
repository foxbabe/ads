package com.sztouyun.advertisingsystem.mapper;

import com.sztouyun.advertisingsystem.model.monitor.ClickOrScanTimes;
import com.sztouyun.advertisingsystem.model.monitor.DateLinkTimes;
import com.sztouyun.advertisingsystem.model.monitor.StoreParticipationStatistics;
import com.sztouyun.advertisingsystem.viewmodel.monitor.ClickOrScanTimesTrendRequest;
import com.sztouyun.advertisingsystem.viewmodel.monitor.MaterialLinkMonitorInfo;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;
import com.sztouyun.advertisingsystem.viewmodel.monitor.AdvertisementDailyLinkMonitorStatistic;

import java.util.List;

@Mapper
public interface AdvertisementDailyLinkMonitorStatisticMapper {
    List<StoreParticipationStatistics> getAllStoreParticipationStatistics(String advertisementId);

    List<ClickOrScanTimes> getAllClickOrScanTimes(String advertisementId);

    List<DateLinkTimes> getAllLinkTimes(ClickOrScanTimesTrendRequest request);

    void saveAdvertisementDailyLinkMonitorStatistic(List<AdvertisementDailyLinkMonitorStatistic> list);

    List<MaterialLinkMonitorInfo> getMaterialLinkMonitorInfo(@Param("advertisementMaterialId") String advertisementMaterialId, @Param("linkType") Integer linkType);
}
