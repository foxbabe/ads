package com.sztouyun.advertisingsystem.service.task.advertisement.operations;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.model.monitor.QAdvertisementDailyDeliveryMonitorStatistic;
import com.sztouyun.advertisingsystem.repository.monitor.AdvertisementDailyDeliveryMonitorStatisticRepository;
import com.sztouyun.advertisingsystem.service.task.advertisement.base.BaseAutoCancelAdvertisementStoreTaskOperation;
import com.sztouyun.advertisingsystem.service.task.advertisement.data.AdvertisementStoreInfo;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class AutoCancelActivatedAdvertisementStoreTaskOperation extends BaseAutoCancelAdvertisementStoreTaskOperation<AdvertisementStoreInfo> {
    @Autowired
    private AdvertisementDailyDeliveryMonitorStatisticRepository deliveryMonitorStatisticRepository;

    private static final QAdvertisementDailyDeliveryMonitorStatistic qDeliveryMonitorStatistic = QAdvertisementDailyDeliveryMonitorStatistic.advertisementDailyDeliveryMonitorStatistic;

    @Override
    public Boolean validate(AdvertisementStoreInfo advertisementStoreInfo) {
        Boolean exists = deliveryMonitorStatisticRepository.exists(q -> q.selectFrom(qDeliveryMonitorStatistic)
                .where(qDeliveryMonitorStatistic.advertisementId.eq(advertisementStoreInfo.getAdvertisement().getId())
                        .and(qDeliveryMonitorStatistic.storeId.eq(advertisementStoreInfo.getStoreInfo().getId()))
                        .and(qDeliveryMonitorStatistic.date.eq(new LocalDate(advertisementStoreInfo.getDate()).minusDays(1).toDate()))
                        .and(qDeliveryMonitorStatistic.displayTimes.gt(0))));
        return exists == null ? Boolean.FALSE : exists;

    }

    @Override
    public String getRemark(AdvertisementStoreInfo advertisementStoreInfo) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constant.DATA_YMD_CN);
        String dateFormat = simpleDateFormat.format(new LocalDate(advertisementStoreInfo.getDate()).minusDays(1).toDate());
        return "广告名称为“"+advertisementStoreInfo.getAdvertisement().getAdvertisementName()+"”的广告，"+dateFormat+"收到了日志，任务自动取消";
    }
}
