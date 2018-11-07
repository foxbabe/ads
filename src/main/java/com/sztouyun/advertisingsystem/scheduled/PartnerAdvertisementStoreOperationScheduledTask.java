package com.sztouyun.advertisingsystem.scheduled;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.mapper.PartnerAdvertisementMapper;
import com.sztouyun.advertisingsystem.model.job.ScheduledJob;
import com.sztouyun.advertisingsystem.model.partner.advertisement.PartnerAdvertisementStatusEnum;
import com.sztouyun.advertisingsystem.model.partner.advertisement.QPartnerAdvertisement;
import com.sztouyun.advertisingsystem.repository.job.ScheduledJobRepository;
import com.sztouyun.advertisingsystem.repository.partner.advertisement.PartnerAdvertisementRepository;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;
import com.sztouyun.advertisingsystem.service.partner.advertisement.PartnerAdvertisementDeliveryRecordService;
import com.sztouyun.advertisingsystem.service.partner.advertisement.PartnerAdvertisementService;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import com.sztouyun.advertisingsystem.utils.UUIDUtils;
import com.sztouyun.advertisingsystem.viewmodel.coorperationPartner.DailyPartnerAdvertisementStoreDisplayInfo;
import com.sztouyun.advertisingsystem.viewmodel.coorperationPartner.WrappedDailyPartnerAdvertisementStoreDisplayInfo;
import lombok.experimental.var;
import org.apache.calcite.linq4j.Linq4j;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.mapreduce.MapReduceOptions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class PartnerAdvertisementStoreOperationScheduledTask extends BaseScheduledTask{

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final QPartnerAdvertisement qPartnerAdvertisement = QPartnerAdvertisement.partnerAdvertisement;
    @Autowired
    private PartnerAdvertisementRepository partnerAdvertisementRepository;
    @Autowired
    private PartnerAdvertisementDeliveryRecordService partnerAdvertisementDeliveryRecordService;
    @Autowired
    private PartnerAdvertisementService partnerAdvertisementService;
    @Autowired
    private PartnerAdvertisementMapper partnerAdvertisementMapper;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ScheduledJobRepository scheduledJobRepository;

    @Scheduled(cron = "${partnerAdvertisementStore.status.jobs.cron.minute}")
    @Transactional
    public void autoTakeOffPartnerAdvertisementDeliveryRecord() {
        var now = new Date().getTime();
        AuthenticationService.setAdminLogin();
        List<String> deliveringAdvertisementIds=partnerAdvertisementRepository.findAll(q->q.select(qPartnerAdvertisement.id).from(qPartnerAdvertisement).where(qPartnerAdvertisement.advertisementStatus.eq(PartnerAdvertisementStatusEnum.Delivering.getValue())));
        Query query= new Query(Criteria.where("partnerAdvertisementId").in(deliveringAdvertisementIds));
        query.addCriteria(Criteria.where("effectiveFinishTime").lt(now));
        partnerAdvertisementDeliveryRecordService.takeOff(query,"在规定的RTB模式时长内，广告未上报监播地址，广告播放无效，系统自动下架广告",null);

        var newDeliveringAdvertisementIds =partnerAdvertisementService.getDeliveringAdvertisementIds(deliveringAdvertisementIds);
        List<String> finishedAdvertisementIds = Linq4j.asEnumerable(deliveringAdvertisementIds).except(Linq4j.asEnumerable(newDeliveringAdvertisementIds)).toList();
        //更新三方广告状态为已完成
        partnerAdvertisementService.finishPartnerAdvertisement(finishedAdvertisementIds);
    }

    @Scheduled(cron = "${partnerAdvertisementStore.daily.statistic.jobs.cron.minute}")
    public void reCalcDailyPartnerStoreAdvertisementDisplayInfo(){
        Date lastDay= LocalDate.now().minusDays(1).toDate();
        calcPartnerStoreAdvertisementDisplayInfo(lastDay,lastDay);
    }

    public void calcPartnerStoreAdvertisementDisplayInfo(Date start,Date end){
        ScheduledJob scheduledJob=new ScheduledJob("calcPartnerStoreAdvertisementDisplayInfo");
        String outputCollectionName= UUIDUtils.generateBase58UUID();
        Date startTime= new LocalDate(start).toDate();
        calcDailyPartnerStoreAdvertisementDisplayInfo(start,end,outputCollectionName);
        int index=0;
        while(!startTime.after(end)){
            while(true){
               try {
                    List<DailyPartnerAdvertisementStoreDisplayInfo> list = getDailyPartnerAdvertisementStoreDisplayInfo(startTime.getTime(), index * Constant.MONGODB_PAGESIZE, Constant.MONGODB_PAGESIZE, outputCollectionName);
                    if (CollectionUtils.isEmpty(list))
                        break;
                    partnerAdvertisementMapper.reCalcPartnerAdvertisementDisplayStatistic(list);
                }catch (Exception e){
                    logger.error("重算合作方广告展示数据异常,页数："+index+"，时间："+DateUtils.dateFormat(startTime,Constant.DATA_YMD_CN),e);
                }
                index++;
            }
            startTime=new LocalDate(startTime).plusDays(1).toDate();
            index=0;
        }
        mongoTemplate.dropCollection(outputCollectionName);
        scheduledJobRepository.save(scheduledJob);
    }

    private List<DailyPartnerAdvertisementStoreDisplayInfo> getDailyPartnerAdvertisementStoreDisplayInfo(Long time,Long skip,Long limit,String collectionName){
        List<AggregationOperation> operations=new ArrayList<>();
        operations.add(match(Criteria.where("value.requestDate").is(time)));
        operations.add(skip(skip));
        operations.add(limit(limit));
        operations.add(project().
                and("value.storeId").as("storeId").
                and("value.partnerId").as("partnerId").
                and("value.requestDate").as("requestDate").
                and("value.advertisementPositionCategory").as("advertisementPositionCategory").
                and("value.displayTimes").as("displayTimes").
                and("value.validTimes").as("validTimes")
        );
        Aggregation aggregation=Aggregation.newAggregation(operations);
        return mongoTemplate.aggregate(aggregation,collectionName,DailyPartnerAdvertisementStoreDisplayInfo.class).getMappedResults();
    }

    private void calcDailyPartnerStoreAdvertisementDisplayInfo(Date start, Date end, String outputCollectionName){
        MapReduceOptions mro=new MapReduceOptions();
        mro.outputCollection(outputCollectionName);
        mongoTemplate.mapReduce(
                new Query(Criteria.where("requestDate").gte(start.getTime()).lte(end.getTime())),
                "partnerAdvertisementDeliveryRecord",
                "classpath:script/partnerAdvertisementDailyStatistic/mapFunc.js",
                "classpath:script/partnerAdvertisementDailyStatistic/reduceFunc.js",
                mro,
                WrappedDailyPartnerAdvertisementStoreDisplayInfo.class
                );
    }
}
