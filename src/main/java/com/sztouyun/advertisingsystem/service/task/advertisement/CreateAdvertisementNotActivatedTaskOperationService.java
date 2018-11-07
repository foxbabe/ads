package com.sztouyun.advertisingsystem.service.task.advertisement;

import com.sztouyun.advertisingsystem.common.operation.IOperationCollection;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.monitor.QAdvertisementDailyDeliveryMonitorStatistic;
import com.sztouyun.advertisingsystem.model.store.StoreInfo;
import com.sztouyun.advertisingsystem.model.system.HistoricalParamConfig;
import com.sztouyun.advertisingsystem.model.system.HistoricalParamConfigTypeEnum;
import com.sztouyun.advertisingsystem.model.task.TaskCategoryEnum;
import com.sztouyun.advertisingsystem.repository.monitor.AdvertisementDailyDeliveryMonitorStatisticRepository;
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

import java.util.Date;
import java.util.Map;


@Service
public class CreateAdvertisementNotActivatedTaskOperationService extends BaseCreateAdvertisementStoreTaskOperationService {
    @Autowired
    private HistoricalParamConfigService historicalParamConfigService;
    @Autowired
    protected AreaService areaService;
    @Autowired
    private AdvertisementDailyDeliveryMonitorStatisticRepository advertisementDailyDeliveryMonitorStatisticRepository;

    private QAdvertisementDailyDeliveryMonitorStatistic qStatistic = QAdvertisementDailyDeliveryMonitorStatistic.advertisementDailyDeliveryMonitorStatistic;

    @Override
    protected TaskCategoryEnum getTaskCategory(AdvertisementStoreInfo data) {
        return TaskCategoryEnum.AdvertisementNotActivated;
    }

    @Override
    protected void onOperating(AdvertisementStoreInfo data, IOperationCollection<AdvertisementStoreInfo, Boolean> operationCollection) {
        HistoricalParamConfig notActivatedDaysConfig = historicalParamConfigService.getHistoricalParamConfigFromCache(HistoricalParamConfigTypeEnum.NOT_ACTIVATED_DAYS, data.getDate());
        if (notActivatedDaysConfig == null) {
            throw new BusinessException("获取不到未激活天数");
        }
        //广告投放中门店曾经激活并且近n天未激活
        operationCollection
                .add(ValidateDisplayDurationOperation.class, new DisplayDurationInfo(data.getAdvertisement(), data.getStoreInfo(),data.getAdvertisement().getEffectiveStartTime(), data.getDate(), notActivatedDaysConfig.getValue().intValue()))
                .add(ValidateLastTaskDurationOperation.class,new TaskDurationInfo(data.getLastTask(),data.getDate(),notActivatedDaysConfig.getValue().intValue()))
                .add(ValidateAdvertisementStorePeriodActivatedOperation.class,new AdvertisementStorePeriodInfo(data,new LocalDate(data.getAdvertisement().getEffectiveStartTime()).toDate(),true))
                .add(ValidateAdvertisementStorePeriodActivatedOperation.class,new AdvertisementStorePeriodInfo(data,new LocalDate(data.getDate()).minusDays(notActivatedDaysConfig.getValue().intValue()).toDate(),false));
    }

    @Override
    protected String getTaskName(AdvertisementStoreInfo data) {
        HistoricalParamConfig notActivatedDaysConfig = historicalParamConfigService.getHistoricalParamConfigFromCache(HistoricalParamConfigTypeEnum.NOT_ACTIVATED_DAYS, data.getDate());
        if (notActivatedDaysConfig == null) {
            throw new BusinessException("获取不到未上架天数");
        }
        LocalDate endLocalDate = new LocalDate(data.getDate());
        LocalDate startLocalDate = endLocalDate.minusDays(notActivatedDaysConfig.getValue().intValue());
        StoreInfo storeInfo = data.getStoreInfo();
        Map<String,String> areaMap = areaService.getAllAreaNames();

        Date lastGetLogDate = advertisementDailyDeliveryMonitorStatisticRepository.findOne(q -> q.select(qStatistic.date).from(qStatistic).where(
                qStatistic.advertisementId.eq(data.getAdvertisement().getId())
                        .and(qStatistic.storeId.eq(data.getStoreInfo().getId()))
                        .and(qStatistic.date.lt(data.getDate()))
                        .and(qStatistic.displayTimes.gt(0))
        ).orderBy(qStatistic.date.desc()).limit(1));

        StringBuffer sb = new StringBuffer();
        sb.append("门店名称: ").append(storeInfo.getStoreName())
                .append("<br />地址: ").append(getAreaName(storeInfo.getProvinceId(), areaMap))
                .append(getAreaName(storeInfo.getCityId(), areaMap))
                .append(getAreaName(storeInfo.getRegionId(), areaMap))
                .append(storeInfo.getStoreAddress())
                .append("<br />广告名称: ").append(data.getAdvertisement().getAdvertisementName())
                .append("<br />描述: ")
                .append(new LocalDateTime(lastGetLogDate).toString("yyyy年MM月dd日"))
                .append("收到过广告激活日志, 后续")
                .append(startLocalDate.toString("yyyy年MM月dd日"))
                .append(" - ")
                .append(endLocalDate.minusDays(1).toString("yyyy年MM月dd日"))
                .append("时间内，广告后台从未收到广告激活日志")
        ;
        return sb.toString();
    }
}
