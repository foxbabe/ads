package com.sztouyun.advertisingsystem.scheduled;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.mapper.AdvertisementDailyLinkMonitorStatisticMapper;
import com.sztouyun.advertisingsystem.mapper.ContractAdvertisementPositionConfigMapper;
import com.sztouyun.advertisingsystem.model.job.ScheduledJob;
import com.sztouyun.advertisingsystem.model.system.AdvertisementPositionCategoryEnum;
import com.sztouyun.advertisingsystem.repository.job.ScheduledJobRepository;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import com.sztouyun.advertisingsystem.viewmodel.monitor.AdvertisementDailyLinkMonitorInfo;
import com.sztouyun.advertisingsystem.viewmodel.monitor.AdvertisementDailyLinkMonitorStatistic;
import com.sztouyun.advertisingsystem.viewmodel.monitor.AdvertisementPositionConfig;
import lombok.experimental.var;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * Created by szty on 2018/7/3.
 */
@Component
public class AdvertisementDailyLinkMonitorScheduled extends BaseScheduledTask {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ContractAdvertisementPositionConfigMapper contractAdvertisementPositionConfigMapper;
    @Autowired
    private AdvertisementDailyLinkMonitorStatisticMapper advertisementDailyLinkMonitorStatisticMapper;
    @Autowired
    private ScheduledJobRepository scheduledJobRepository;

    @Scheduled(cron = "${advertisement.link.monitor.cron.minute}")
    public void updateAdvertisementDailyLinkMonitorStatistic() {
        ScheduledJob scheduledJob = new ScheduledJob(AdvertisementDailyLinkMonitorScheduled.class.getName());
        Date now=new Date();
        Date start=getLastSucceedDate(AdvertisementDailyLinkMonitorScheduled.class.getName());
        if(start==null){
            start=LocalDate.parse("2018-07-03").toDate();
        }
        StringBuffer remark=new StringBuffer();
        try {
            while (start.before(now)){
                var  endTime= new LocalDate(start).plusDays(1).toDate();
                if(endTime.after(now)){
                    endTime = now;
                }
                updateAdvertisementDailyLinkMonitorStatistic(start,endTime);
                start=endTime;
            }
            remark.append("广告链接监控汇总任务完成");
        } catch (Exception e) {
            scheduledJob.setFailedTaskSize(scheduledJob.getFailedTaskSize()+1);
            scheduledJob.setSuccessed(false);
            remark.append("#####广告链接监控汇总任务失败#####");
            logger.error(e.getMessage(),e);
        }
        scheduledJob.setRemark("开始时间:"+ DateUtils.dateFormat(now,Constant.DATE_TIME_CN)+"结束时间:"+ DateUtils.dateFormat(new Date(),Constant.DATE_TIME_CN)+remark.toString());
        scheduledJob.setCreatedTime(now);
        scheduledJobRepository.save(scheduledJob);
    }

    @Transactional
    private void updateAdvertisementDailyLinkMonitorStatistic(Date start,Date end){
        Long beginTime=start.getTime();
        Long endTime=end.getTime();
        Long pageIndex = 0L;
        while(true){
            List<AdvertisementDailyLinkMonitorInfo> list=getAdvertisementDailyLinkMonitorInfo(beginTime, endTime, (pageIndex * Constant.MONGODB_PAGESIZE), Constant.MONGODB_PAGESIZE);
            if(list==null || list.isEmpty())
                break;
            List<AdvertisementDailyLinkMonitorStatistic> statistics =new ArrayList<>();
            list.stream().forEach(item->{
                AdvertisementDailyLinkMonitorStatistic advertisementDailyLinkMonitorStatistic=ApiBeanUtils.copyProperties(item,AdvertisementDailyLinkMonitorStatistic.class);
                advertisementDailyLinkMonitorStatistic.setDate(LocalDateTime.fromDateFields(start).toLocalDate().toDate());
                AdvertisementPositionConfig config=contractAdvertisementPositionConfigMapper.getAdvertisementPositionConfig(item.getMaterialUrlStepId());
                if(config==null)
                    return;
                AdvertisementPositionCategoryEnum advertisementPositionCategoryEnum=AdvertisementPositionCategoryEnum.getCategoryEnumByPositionAndTerminal(config.getAdvertisementPositionType(),config.getTerminalType());
                advertisementDailyLinkMonitorStatistic.setAdvertisementPositionCategory(advertisementPositionCategoryEnum.getValue());
                advertisementDailyLinkMonitorStatistic.setLinkType(config.getLinkType());
                advertisementDailyLinkMonitorStatistic.setStepType(config.getStepType());
                statistics.add(advertisementDailyLinkMonitorStatistic);
            });
            if(!statistics.isEmpty()) {
                advertisementDailyLinkMonitorStatisticMapper.saveAdvertisementDailyLinkMonitorStatistic(statistics);
            }
            pageIndex++;
        }
    }



    private List<AdvertisementDailyLinkMonitorInfo> getAdvertisementDailyLinkMonitorInfo(Long beginTime, Long endTime, Long skip, Long limit) {
        Aggregation agg = newAggregation(
                match(Criteria.where("createdTime").gte(beginTime).lt(endTime)),
                match(Criteria.where("valid").is(Boolean.TRUE)),
                group("storeId","action","urlStepId")
                        .count().as("triggerTimes"),
                project("storeId","action","triggerTimes").and("urlStepId").as("materialUrlStepId"),
                skip(skip),
                limit(limit)
        );
        return mongoTemplate.aggregate(agg, "materialMonitorInfo", AdvertisementDailyLinkMonitorInfo.class).getMappedResults();
    }
}
