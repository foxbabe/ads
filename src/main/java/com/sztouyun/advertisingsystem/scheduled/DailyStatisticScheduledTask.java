package com.sztouyun.advertisingsystem.scheduled;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.mapper.CustomerMapper;
import com.sztouyun.advertisingsystem.model.common.DailyStatisticTypeEnum;
import com.sztouyun.advertisingsystem.model.mongodb.DailyStatistic;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by szty on 2018/9/5.
 */
@Component
public class DailyStatisticScheduledTask extends BaseScheduledTask {
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Scheduled(cron = "${customer.info.daily.statistic.cron}")
    public void statisticDailyCustomerInfo() {
        Date now=new Date();
        statisticDailyCustomerInfo(now,now);
    }

    public void statisticDailyCustomerInfo(Date start, Date end){
        Date executeTime=new Date();
        LocalDate startDate=new LocalDate(start);
        List<DailyStatistic> dailyStatisticList =new ArrayList<>();
        try {
            mongoTemplate.remove(new Query(Criteria.where("date").gte(startDate.toDate().getTime()).lte(end.getTime()).and("statisticType").is(DailyStatisticTypeEnum.CustomerCount.getValue())),DailyStatistic.class);
            do {
                Long count=customerMapper.getCustomerCount(startDate.plusDays(1).toDate());
                dailyStatisticList.add(new DailyStatistic(startDate.toDate().getTime(),count.intValue(), DailyStatisticTypeEnum.CustomerCount.getValue()));
                startDate=startDate.plusDays(1);
                if(dailyStatisticList.size()%Constant.MONGODB_PAGESIZE==0){
                    mongoTemplate.insert(dailyStatisticList,DailyStatistic.class);
                    dailyStatisticList.clear();
                }
            }while (!startDate.toDate().after(end));
            if(!CollectionUtils.isEmpty(dailyStatisticList)){
                mongoTemplate.insert(dailyStatisticList,DailyStatistic.class);
            }
        }catch (Exception e){
            logger.error("统计客户数量异常,执行时间:"+ DateUtils.dateFormat(executeTime, Constant.DATE_TIME_CN)+",开始时间："+DateUtils.dateFormat(start, Constant.DATA_YMD_CN)+",结束时间："+DateUtils.dateFormat(end, Constant.DATA_YMD_CN));
        }

    }
}
