package com.sztouyun.advertisingsystem.service.task.advertisement;

import com.sztouyun.advertisingsystem.common.operation.IOperationCollection;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.store.StoreInfo;
import com.sztouyun.advertisingsystem.model.system.HistoricalParamConfig;
import com.sztouyun.advertisingsystem.model.system.HistoricalParamConfigTypeEnum;
import com.sztouyun.advertisingsystem.model.task.TaskCategoryEnum;
import com.sztouyun.advertisingsystem.service.system.AreaService;
import com.sztouyun.advertisingsystem.service.system.HistoricalParamConfigService;
import com.sztouyun.advertisingsystem.service.task.advertisement.base.BaseCreateAdvertisementStoreTaskOperationService;
import com.sztouyun.advertisingsystem.service.task.advertisement.data.AdvertisementStoreInfo;
import com.sztouyun.advertisingsystem.service.task.advertisement.data.AdvertisementStorePeriodInfo;
import com.sztouyun.advertisingsystem.service.task.advertisement.data.DisplayDurationInfo;
import com.sztouyun.advertisingsystem.service.task.advertisement.data.TaskDurationInfo;
import com.sztouyun.advertisingsystem.service.task.advertisement.operations.ValidateAdvertisementStorePeriodActivatedOperation;
import com.sztouyun.advertisingsystem.service.task.advertisement.operations.ValidateDisplayDurationOperation;
import com.sztouyun.advertisingsystem.service.task.advertisement.operations.ValidateLastTaskDurationOperation;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CreateAdvertisementNotDisplayTaskOperationService extends BaseCreateAdvertisementStoreTaskOperationService {
    @Autowired
    private HistoricalParamConfigService historicalParamConfigService;
    @Autowired
    protected AreaService areaService;
    @Override
    protected TaskCategoryEnum getTaskCategory(AdvertisementStoreInfo data) {
        return TaskCategoryEnum.AdvertisementNotPlay;
    }

    @Override
    protected void onOperating(AdvertisementStoreInfo data, IOperationCollection<AdvertisementStoreInfo, Boolean> operationCollection) {
        HistoricalParamConfig notPlayDaysConfig = historicalParamConfigService.getHistoricalParamConfigFromCache(HistoricalParamConfigTypeEnum.NOT_PLAY_DAYS, data.getDate());
        if (notPlayDaysConfig == null) {
            throw new BusinessException("获取不到未上架天数");
        }

        //广告投放中门店从未激活并且近n天未激活
        operationCollection
                .add(ValidateDisplayDurationOperation.class, new DisplayDurationInfo(data.getAdvertisement(), data.getStoreInfo(),data.getAdvertisement().getEffectiveStartTime(), data.getDate(), notPlayDaysConfig.getValue().intValue()))
                .add(ValidateLastTaskDurationOperation.class,new TaskDurationInfo(data.getLastTask(),data.getDate(),notPlayDaysConfig.getValue().intValue()))
                .add(ValidateAdvertisementStorePeriodActivatedOperation.class,new AdvertisementStorePeriodInfo(data,new LocalDate(data.getAdvertisement().getEffectiveStartTime()).toDate(),false))
                .add(ValidateAdvertisementStorePeriodActivatedOperation.class,new AdvertisementStorePeriodInfo(data,new LocalDateTime(data.getDate()).minusDays(notPlayDaysConfig.getValue().intValue()).toDate(),false));
    }

    @Override
    protected String getTaskName(AdvertisementStoreInfo data) {
        HistoricalParamConfig notPlayDaysConfig = historicalParamConfigService.getHistoricalParamConfigFromCache(HistoricalParamConfigTypeEnum.NOT_PLAY_DAYS, data.getDate());
        if (notPlayDaysConfig == null) {
            throw new BusinessException("获取不到未上架天数");
        }
        LocalDateTime endLocalDateTime = new LocalDateTime(data.getDate());
        LocalDateTime startLocalDateTime = endLocalDateTime.minusDays(notPlayDaysConfig.getValue().intValue());
        StoreInfo storeInfo = data.getStoreInfo();
        Map<String,String> areaMap = areaService.getAllAreaNames();

        StringBuffer sb = new StringBuffer();
        sb.append("门店名称: ").append(storeInfo.getStoreName())
                .append("<br />地址: ").append(getAreaName(storeInfo.getProvinceId(), areaMap))
                .append(getAreaName(storeInfo.getCityId(), areaMap))
                .append(getAreaName(storeInfo.getRegionId(), areaMap))
                .append(storeInfo.getStoreAddress())
                .append("<br />广告名称: ").append(data.getAdvertisement().getAdvertisementName())
                .append("<br />描述: ")
                .append(startLocalDateTime.toString("yyyy年MM月dd日"))
                .append(" - ")
                .append(endLocalDateTime.minusDays(1).toString("yyyy年MM月dd日"))
                .append("时间内，广告后台从未收到过广告激活日志")
        ;
        return sb.toString();
    }
}
