package com.sztouyun.advertisingsystem.service.partner.advertismentSource;

import com.sztouyun.advertisingsystem.model.mongodb.PartnerAdvertisementRequestResultEnum;
import com.sztouyun.advertisingsystem.model.monitor.PartnerDailyStoreMonitorStatistic;
import com.sztouyun.advertisingsystem.model.system.AdvertisementPositionCategoryEnum;
import com.sztouyun.advertisingsystem.service.partner.advertisement.PartnerAdvertisementRequestLogService;
import lombok.experimental.var;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BasePartnerAdvertisementService implements IPartnerAdvertisementService  {
    private final AdvertisementSourceEnum advertisementSource;
    public BasePartnerAdvertisementService(AdvertisementSourceEnum advertisementSource){
        this.advertisementSource = advertisementSource;
    }

    @Autowired
    private PartnerAdvertisementRequestLogService partnerAdvertisementRequestLogService;

    protected void savePartnerAdvertisementRequestLog(String partnerId, String storeId, AdvertisementPositionCategoryEnum advertisementPositionCategory, PartnerAdvertisementRequestResultEnum result){
        var storeMonitorStatistic  =new PartnerDailyStoreMonitorStatistic(partnerId, LocalDate.now().toDate(),storeId,advertisementPositionCategory.getValue());
        switch (result) {
            case GetAd:
                storeMonitorStatistic.setGetAdTimes(1);
                break;
            case GetNoAd:
                storeMonitorStatistic.setGetNoAdTimes(1);
                break;
            case ApiError:
                storeMonitorStatistic.setApiErrorTimes(1);
                break;
        }
        partnerAdvertisementRequestLogService.sendPartnerAdvertisementRequestLog(storeMonitorStatistic);
    }

    protected String getPartnerAdvertisementId(String thirdPartId){
        return advertisementSource.getValue()+"X"+thirdPartId;
    }
}
