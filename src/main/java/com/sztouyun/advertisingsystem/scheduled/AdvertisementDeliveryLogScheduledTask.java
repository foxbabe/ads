package com.sztouyun.advertisingsystem.scheduled;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.mapper.AdvertisementStoreMonitorMapper;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementStatusEnum;
import com.sztouyun.advertisingsystem.model.advertisement.QAdvertisement;
import com.sztouyun.advertisingsystem.model.job.QScheduledJob;
import com.sztouyun.advertisingsystem.model.job.ScheduledJob;
import com.sztouyun.advertisingsystem.model.system.AdvertisementPositionCategoryEnum;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementRepository;
import com.sztouyun.advertisingsystem.repository.job.ScheduledJobRepository;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.AdvertisementInfoModel;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.monitor.AdvertisementDailyDeliveryMonitor;
import com.sztouyun.advertisingsystem.viewmodel.monitor.AdvertisementMaterialDailyDisplayTimes;
import com.sztouyun.advertisingsystem.viewmodel.monitor.DailyDeliveryMonitorStatistic;
import com.sztouyun.advertisingsystem.viewmodel.monitor.DeliveryMonitorStatistic;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;


@Component
public class AdvertisementDeliveryLogScheduledTask extends BaseScheduledTask{
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ScheduledJobRepository scheduledJobRepository;
    @Autowired
    private AdvertisementStoreMonitorMapper advertisementStoreMonitorMapper;
    @Autowired
    private AdvertisementRepository advertisementRepository;

    private static final  QScheduledJob qScheduledJob = QScheduledJob.scheduledJob;
    private static final QAdvertisement qAdvertisement=QAdvertisement.advertisement;

    @Scheduled(cron = "${delivery.monitor.jobs.cron.minute}")
    public void updateDeliveryMonitor() {
        logger.info("定时任务：更新门店监控统计");
        AuthenticationService.setAdminLogin();
        long beginTime = getScheduleBeginTime();
        Date now = new Date();
        long endTime =now.getTime();

        ScheduledJob scheduledJob = new ScheduledJob(AdvertisementDeliveryLogScheduledTask.class.getName());
        StringBuffer remark=new StringBuffer();
        try {
            updateDeliveryMonitorTask(beginTime, endTime);
            remark.append("监控日志计算定时任务成功");
        } catch (Exception e) {
            scheduledJob.setFailedTaskSize(scheduledJob.getFailedTaskSize()+1);
            scheduledJob.setSuccessed(false);
            remark.append("#####监控日志计算定时任务失败#####");
            logger.error(e.getMessage());
        }
        scheduledJob.setRemark("开始时间:"+ DateUtils.dateFormat(now,Constant.DATE_TIME_CN)+"结束时间:"+ DateUtils.dateFormat(new Date(),Constant.DATE_TIME_CN)+remark.toString());
        scheduledJob.setCreatedTime(now);
        scheduledJobRepository.save(scheduledJob);
    }

    public Long getScheduleBeginTime() {
        ScheduledJob lastSuccessScheduledJob = scheduledJobRepository.findOne(s -> s.select(qScheduledJob).from(qScheduledJob)
                .where(qScheduledJob.jobName.eq(AdvertisementDeliveryLogScheduledTask.class.getName()).and(qScheduledJob.isSuccessed.isTrue()))
                .orderBy(qScheduledJob.createdTime.desc())
                .limit(1));
        if(lastSuccessScheduledJob == null)
            return DateTime.parse("2017-11-01").toDate().getTime();

        return lastSuccessScheduledJob.getCreatedTime().getTime();
    }

    @Transactional
    public void updateDeliveryMonitorTask(Long beginTime, Long endTime) {
        List<DeliveryMonitorStatistic> advertisementMonitorStatistic = getMonitorInfo(beginTime, endTime, "advertisementId");
        if(!CollectionUtils.isEmpty(advertisementMonitorStatistic)) {
            advertisementStoreMonitorMapper.updateAdvertisementMonitorIfo(advertisementMonitorStatistic);
        }
        updateAdvertisementDailyDeliveryMonitorInfo(beginTime,endTime,true);
        updateAdvertisementDailyDeliveryMonitorInfo(beginTime,endTime,false);
    }

    private List<DeliveryMonitorStatistic> getMonitorInfo(Long beginTime, Long endTime, String... groupKey) {
        List<String> projectList = new ArrayList<String>(){{
            addAll(Arrays.asList(groupKey));
            add("displayTimes");
        }};
        Aggregation agg = newAggregation(
                match(Criteria.where("createdTime").gte(beginTime).lt(endTime)),
                group(groupKey)
                        .sum("displayTimes").as("displayTimes"),
                project(projectList.toArray(new String[projectList.size()]))

        );
        return mongoTemplate.aggregate(agg, "advertisementDeliveryLog", DeliveryMonitorStatistic.class).getMappedResults();
    }

    private List<AdvertisementMaterialDailyDisplayTimes> getAdvertisementDailyDeliveryMonitorInfo(Long beginTime, Long endTime, Long skip, Long limit,Boolean isCashRegister) {
        StringBuffer groupFields=new StringBuffer("dailyDisplayTimes.datetime,advertisementMaterialId,advertisementPositionType,terminalType");
        StringBuffer projectFields=new StringBuffer("dailyDisplayTimes.datetime,advertisementMaterialId,advertisementPositionType,terminalType,times");
        if(isCashRegister){
            groupFields.append(",storeId");
            projectFields.append(",storeId");
        }
        Aggregation agg = newAggregation(
                match(Criteria.where("createdTime").gte(beginTime).lt(endTime)),
                match(Criteria.where("storeId").exists(isCashRegister)),
                unwind("dailyDisplayTimes"),
                group(groupFields.toString().split(Constant.SEPARATOR))
                        .sum("dailyDisplayTimes.times").as("times"),
                project(projectFields.toString().split(Constant.SEPARATOR)),
                skip(skip),
                limit(limit)
        );
        return mongoTemplate.aggregate(agg, "advertisementDeliveryLog", AdvertisementMaterialDailyDisplayTimes.class).getMappedResults();
    }

    private void updateAdvertisementDailyDeliveryMonitorInfo(Long beginTime,Long endTime,Boolean isCashRegister){
        Long pageIndex = 0L;
//        Date today = LocalDate.now().toDate();
//        List<String> orderIds = new ArrayList<>();
        while(true) {
            List<AdvertisementMaterialDailyDisplayTimes> advertisementDailyDeliveryMonitorInfo = getAdvertisementDailyDeliveryMonitorInfo(beginTime, endTime, (pageIndex * Constant.MONGODB_PAGESIZE), Constant.MONGODB_PAGESIZE,isCashRegister);
            if(advertisementDailyDeliveryMonitorInfo == null || advertisementDailyDeliveryMonitorInfo.size() == 0)
                break;
            List<DailyDeliveryMonitorStatistic> dailyDeliveryMonitorStatistics = new ArrayList<>();
//            List<OrderDailyDeliveryMonitorStatistic> orderDailyDeliveryMonitorStatistics = new ArrayList<>();
            for (AdvertisementMaterialDailyDisplayTimes a :advertisementDailyDeliveryMonitorInfo){
                AdvertisementInfoModel advertisementInfoModel =advertisementStoreMonitorMapper.getAdvertisementInfo(a.getAdvertisementMaterialId());
                if(advertisementInfoModel !=null){
                    AdvertisementPositionCategoryEnum advertisementPositionCategoryEnum= AdvertisementPositionCategoryEnum.getCategoryEnumByPositionAndTerminal(advertisementInfoModel.getAdvertisementPositionType(),advertisementInfoModel.getTerminalType());
                    DailyDeliveryMonitorStatistic dailyDeliveryMonitorStatistic = new DailyDeliveryMonitorStatistic();
                    dailyDeliveryMonitorStatistic.setAdvertisementId(advertisementInfoModel.getAdvertisementId());
                    dailyDeliveryMonitorStatistic.setAdvertisementMaterialId(a.getAdvertisementMaterialId());
                    dailyDeliveryMonitorStatistic.setContractId(advertisementInfoModel.getContractId());
                    dailyDeliveryMonitorStatistic.setDate(new Date(a.getDatetime()));
                    dailyDeliveryMonitorStatistic.setDisplayTimes(a.getTimes());
                    dailyDeliveryMonitorStatistic.setStoreId(a.getStoreId());
                    dailyDeliveryMonitorStatistic.setTerminalType(advertisementInfoModel.getTerminalType());
                    dailyDeliveryMonitorStatistic.setAdvertisementPositionCategory(advertisementPositionCategoryEnum.getValue());
                    dailyDeliveryMonitorStatistic.setPositionType(a.getAdvertisementPositionType());
                    dailyDeliveryMonitorStatistics.add(dailyDeliveryMonitorStatistic);
                }else{
                    //订单只统计当天的
//                    if(!a.getDatetime().equals(today.getTime()))
//                        continue;

//                    advertisementInfoModel =partnerAdvertisementMonitorStatisticMapper.getOrderAdvertisementInfo(a.getAdvertisementMaterialId());
//                    if(advertisementInfoModel !=null){
//                        OrderDailyDeliveryMonitorStatistic orderDailyDeliveryMonitorStatistic = new OrderDailyDeliveryMonitorStatistic();
//                        orderDailyDeliveryMonitorStatistic.setDate(today);
//                        orderDailyDeliveryMonitorStatistic.setDisplayTimes(a.getTimes());
//                        orderDailyDeliveryMonitorStatistic.setOrderId(advertisementInfoModel.getOrderId());
//                        orderDailyDeliveryMonitorStatistic.setOrderMaterialId(advertisementInfoModel.getAdvertisementMaterialId());
//                        orderDailyDeliveryMonitorStatistic.setStoreId(a.getStoreId());
//                        orderDailyDeliveryMonitorStatistics.add(orderDailyDeliveryMonitorStatistic);
//                    }
                }
            }
            //更新操作
            if(!dailyDeliveryMonitorStatistics.isEmpty()){
                advertisementStoreMonitorMapper.saveAdvertisementDailyDeliveryMonitorInfo(new AdvertisementDailyDeliveryMonitor(new Date(endTime),dailyDeliveryMonitorStatistics));
            }
//            if(!orderDailyDeliveryMonitorStatistics.isEmpty()){
//                partnerAdvertisementMonitorStatisticMapper.saveOrderDailyDeliveryLog(orderDailyDeliveryMonitorStatistics);
//                orderIds.addAll(Linq4j.asEnumerable(orderDailyDeliveryMonitorStatistics).select(a->a.getOrderId()).toList());
//            }
            pageIndex +=1;
        }
//        if(!orderIds.isEmpty()){
//            partnerAdvertisementMonitorStatisticMapper.updateOrderDailyStoreActiveCountAndDisplayTimes(new OrderDailyMonitorDto(orderIds,today));
//            partnerAdvertisementMonitorStatisticMapper.updatePartnerDeliveryMonitorInfo(orderIds);
//        }
    }

    @Scheduled(cron = "${delivery.displayTimes.monitor.jobs.cron}")
    public void reCalcDisplayTimes(){
        Long count= advertisementRepository.count(qAdvertisement.advertisementStatus.eq(AdvertisementStatusEnum.Delivering.getValue()));
        Integer index=0;
        do{
            MyPageRequest myPageRequest=new MyPageRequest(index,Constant.CALC_AD_DISPLAYTIMES_IDS_SIZE);
            updateDeliveringAdvertisementDisplayTimes(myPageRequest);
        }while ((++index)*Constant.CALC_AD_DISPLAYTIMES_IDS_SIZE<count);
    }

    @Transactional
    private void updateDeliveringAdvertisementDisplayTimes(MyPageRequest pageable) {
        Page<String> pages= advertisementRepository.findAll(q -> q.select(qAdvertisement.id).from(qAdvertisement).where(
                qAdvertisement.advertisementStatus.eq(AdvertisementStatusEnum.Delivering.getValue())),pageable);
        List<String> advertisementIds=pages.getContent();
        if(!CollectionUtils.isEmpty(advertisementIds)){
            try {
                advertisementStoreMonitorMapper.reCalcAdvertisementDisplayTimes(advertisementIds);
            }catch (Exception e){
                logger.error("重算广告展示次数异常，时间："+ DateUtils.dateFormat(new Date(),Constant.DATETIME)+",index="+pageable.getPageNumber(),e);
            }

        }
    }
}
