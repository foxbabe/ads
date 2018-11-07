package com.sztouyun.advertisingsystem.service.task.advertisement.operations;

import com.sztouyun.advertisingsystem.common.operation.IValidationOperation;
import com.sztouyun.advertisingsystem.model.monitor.QAdvertisementDailyDeliveryMonitorStatistic;
import com.sztouyun.advertisingsystem.repository.monitor.AdvertisementDailyDeliveryMonitorStatisticRepository;
import com.sztouyun.advertisingsystem.service.task.advertisement.data.AdvertisementStorePeriodInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidateAdvertisementStorePeriodActivatedOperation implements IValidationOperation<AdvertisementStorePeriodInfo> {
    @Autowired
    private AdvertisementDailyDeliveryMonitorStatisticRepository advertisementDailyDeliveryMonitorStatisticRepository;
    private QAdvertisementDailyDeliveryMonitorStatistic qStatistic = QAdvertisementDailyDeliveryMonitorStatistic.advertisementDailyDeliveryMonitorStatistic;

    @Override
    public Boolean operate(AdvertisementStorePeriodInfo data) {
        //广告门店周期内的是否展示
        boolean activated=  advertisementDailyDeliveryMonitorStatisticRepository.exists(qStatistic.advertisementId.eq(data.getAdvertisement().getId())
                .and(qStatistic.storeId.eq(data.getStoreInfo().getId())
                .and(qStatistic.date.goe(data.getStartTime())
                .and(qStatistic.date.lt(data.getDate()))
                .and(qStatistic.displayTimes.goe(0)))));
        return data.getActivated().equals(activated);
    }
}
