package com.sztouyun.advertisingsystem.service.profitshare.operations;

import com.sztouyun.advertisingsystem.viewmodel.adProfitShare.DailyDisplayTimes;
import com.sztouyun.advertisingsystem.model.common.ComparisonTypeEnum;
import com.sztouyun.advertisingsystem.service.profitshare.operations.base.BaseAdvertisementProfitShareValidationOperation;
import com.sztouyun.advertisingsystem.service.profitshare.operations.data.AdvertisementValidationRuleData;
import com.sztouyun.advertisingsystem.service.rule.RuleTypeEnum;
import com.sztouyun.advertisingsystem.service.rule.base.data.RuleConfig;
import com.sztouyun.advertisingsystem.model.common.UnitEnum;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class ValidateStoreAdvertisementDeliveryLogOperation extends BaseAdvertisementProfitShareValidationOperation {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    protected RuleTypeEnum getRuleType(AdvertisementValidationRuleData ruleData) {
        return RuleTypeEnum.ValidateStoreAdvertisementDeliveryLog;
    }

    @Override
    protected Double getFactValue(AdvertisementValidationRuleData ruleData) {
        Long time= DateUtils.getDateStartByZero(ruleData.getDateTime()).getTime();
        Integer result=0;
        Aggregation agg = newAggregation(
                match(Criteria.where("advertisementId").is(ruleData.getAdvertisement().getId())),
                match(Criteria.where("storeId").is(ruleData.getStore().getId()).and("dailyDisplayTimes").elemMatch(Criteria.where("datetime").is(time))),
                project("dailyDisplayTimes.datetime","dailyDisplayTimes.times")
        );
        List<DailyDisplayTimes> list=mongoTemplate.aggregate(agg, "advertisementDeliveryLog", DailyDisplayTimes.class).getMappedResults();
        for (DailyDisplayTimes dailyDisplayTimes:list) {
            if(dailyDisplayTimes.getDatetime().equals(time))
                result+=dailyDisplayTimes.getTimes();
        }
        return result.doubleValue();
    }

    @Override
    protected RuleConfig getRuleConfig(AdvertisementValidationRuleData ruleData) {
        RuleConfig  ruleConfig = new RuleConfig();
        ruleConfig.setComparisonType(ComparisonTypeEnum.GT.getValue());
        ruleConfig.setStandard(0D);
        ruleConfig.setUnit(UnitEnum.Times.getValue());
        return ruleConfig;
    }
}
