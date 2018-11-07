package com.sztouyun.advertisingsystem.service.system;

import com.querydsl.core.BooleanBuilder;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.monitor.QPartnerDailyStoreMonitorStatistic;
import com.sztouyun.advertisingsystem.model.system.*;
import com.sztouyun.advertisingsystem.repository.monitor.PartnerDailyStoreMonitorStatisticRepository;
import com.sztouyun.advertisingsystem.repository.system.HistoricalParamConfigRepository;
import com.sztouyun.advertisingsystem.repository.system.SystemOperationLogRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.service.rule.RuleTypeEnum;
import com.sztouyun.advertisingsystem.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@CacheConfig(cacheNames = "historicalParamConfig")
@Service
public class HistoricalParamConfigService extends BaseService {

    private final QHistoricalParamConfig qHistoricalParamConfig =QHistoricalParamConfig.historicalParamConfig;

    @Autowired
    private HistoricalParamConfigRepository historicalParamConfigRepository;
    @Autowired
    private SystemOperationLogRepository systemOperationLogRepository;
    @Autowired
    private PartnerDailyStoreMonitorStatisticRepository partnerDailyStoreMonitorStatisticRepository;
    private final static QPartnerDailyStoreMonitorStatistic qPartnerDailyStoreMonitorStatistic=QPartnerDailyStoreMonitorStatistic.partnerDailyStoreMonitorStatistic;

    @Transactional
    @CacheEvict(allEntries = true)
    public void createHistoricalParamConfig(HistoricalParamConfig historicalParamConfig) {
        HistoricalParamConfigTypeEnum historicalParamConfigType = EnumUtils.toEnum(historicalParamConfig.getType(),HistoricalParamConfigTypeEnum.class);
        HistoricalParamConfig existHistoricalParamConfig = getHistoricalParamConfig(
                historicalParamConfig.getType(),historicalParamConfigType.getGroup().getValue(), new Date(),historicalParamConfig.getObjectId());
        if (existHistoricalParamConfig != null) {
            existHistoricalParamConfig.setUpdatedTime(new Date());
            historicalParamConfigRepository.save(existHistoricalParamConfig);
            historicalParamConfig
                    .setCreatedTime(existHistoricalParamConfig.getUpdatedTime());
        }
        if(historicalParamConfigType.equals(HistoricalParamConfigTypeEnum.PARTNER_PROFIT_ECPM) )
        {
            if(org.springframework.util.StringUtils.isEmpty(historicalParamConfig.getObjectId()))
                throw new BusinessException("合作方不能为空");
            if(existHistoricalParamConfig != null)
                throw new BusinessException("已存在该合作方的收益配置");
        }
        historicalParamConfig.setConfigGroup(historicalParamConfigType.getGroup().getValue());
        historicalParamConfig.setUpdatedTime(DateUtils.getMaxDate());
        historicalParamConfigRepository.save(historicalParamConfig);

        if(historicalParamConfigType.getGroup().equals(HistoricalParamConfigGroupEnum.Task))
            systemOperationLogRepository.save(new SystemOperationLog(SystemOperationEnum.TaskConfig.getValue()));
    }

    @Transactional
    @CacheEvict(allEntries = true)
    public void updateHistoricalParamConfig(HistoricalParamConfig historicalParamConfig) {

        HistoricalParamConfig existHistoricalParamConfig = historicalParamConfigRepository.findOne(historicalParamConfig.getId());
        if (existHistoricalParamConfig == null)
            throw new  BusinessException("配置不存在");
        HistoricalParamConfigTypeEnum historicalParamConfigType = EnumUtils.toEnum(historicalParamConfig.getType(),HistoricalParamConfigTypeEnum.class);
        if(!historicalParamConfigType.getGroup().equals(HistoricalParamConfigGroupEnum.PartnerProfitMode))
            throw new BusinessException("该配置不允许编辑");
        existHistoricalParamConfig.setUpdatedTime(new Date());
        historicalParamConfigRepository.save(existHistoricalParamConfig);

        historicalParamConfig.setConfigGroup(historicalParamConfigType.getGroup().getValue());
        historicalParamConfig.setUpdatedTime(DateUtils.getMaxDate());
        historicalParamConfig.setId(UUIDUtils.generateBase58UUID());
        historicalParamConfigRepository.save(historicalParamConfig);

        if(historicalParamConfigType.getGroup().equals(HistoricalParamConfigGroupEnum.Task))
            systemOperationLogRepository.save(new SystemOperationLog(SystemOperationEnum.TaskConfig.getValue()));
    }

    public HistoricalParamConfig getHistoricalParamConfigFromCache(RuleTypeEnum ruleType, Date date) {
        return getHistoricalParamConfigFromCache(getHistoricalParamConfigTypeEnum(ruleType),date);
    }

    public HistoricalParamConfig getHistoricalParamConfigFromCache(HistoricalParamConfigTypeEnum type, Date date) {
        return getHistoricalParamConfigFromCache(type.getValue(),type.getGroup().getValue(),date,"");
    }

    @Cacheable(key = "(#p0!=null?#p0+'_':'')+#p1+'_'+#p2.time+'_'+#p3")
    public HistoricalParamConfig getHistoricalParamConfigFromCache(Integer type,Integer group, Date date,String objectId) {
        return getHistoricalParamConfig(type,group,date,objectId);
    }

    public List<HistoricalParamConfig> getHistoricalParamConfigGroup(HistoricalParamConfigGroupEnum group,Date date,String objectId) {
        List<HistoricalParamConfig> result = new ArrayList<HistoricalParamConfig>();
        Arrays.stream(HistoricalParamConfigTypeEnum.values()).forEach(r -> {
            if(!r.getGroup().equals(group))
                return;
            HistoricalParamConfig advertisementProfitShareConfig = getHistoricalParamConfigFromCache(
                    r.getValue(),r.getGroup().getValue(),date,objectId);
            if (advertisementProfitShareConfig != null) {
                result.add(advertisementProfitShareConfig);
            }
        });
        return result;
    }

    public HistoricalParamConfig getHistoricalParamConfig(Integer type,Integer group, Date date,String objectId) {
        BooleanBuilder booleanBuilder=new BooleanBuilder();
        booleanBuilder.and(qHistoricalParamConfig.configGroup
                .eq(group)).and(qHistoricalParamConfig.createdTime.loe(date))
                .and(qHistoricalParamConfig.updatedTime.gt(date));
        if(type!=null){
            booleanBuilder.and(qHistoricalParamConfig.type.eq(type));
        }
        if(!org.springframework.util.StringUtils.isEmpty(objectId)){
            booleanBuilder.and(qHistoricalParamConfig.objectId.eq(objectId));
        }
        return historicalParamConfigRepository.findOne(booleanBuilder);
    }


    private HistoricalParamConfigTypeEnum getHistoricalParamConfigTypeEnum(RuleTypeEnum ruleTypeEnum){
        switch (ruleTypeEnum){
            case ValidateStoreOrder:
                return HistoricalParamConfigTypeEnum.ORDERS_MONTHLY_AVERAGE;
            case ValidateStoreOpeningTime:
                return HistoricalParamConfigTypeEnum.BOOT_TIME;
        }
        throw  new BusinessException("找不到规则对应的配置");
    }

    @Transactional
    @CacheEvict(allEntries = true)
    public void delete(String id){
        HistoricalParamConfig historicalParamConfig=historicalParamConfigRepository.findOne(id);
        if(historicalParamConfig==null)
            throw new BusinessException("配置ID无效");
        if(partnerDailyStoreMonitorStatisticRepository.exists(qPartnerDailyStoreMonitorStatistic.partnerId.eq(historicalParamConfig.getObjectId())))
            throw new BusinessException("该配置有业务关联不允许删除");
        historicalParamConfigRepository.delete(id);
    }

}
