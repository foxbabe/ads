package com.sztouyun.advertisingsystem.service.job;

import com.querydsl.core.BooleanBuilder;
import com.sztouyun.advertisingsystem.common.thread.ThreadPool;
import com.sztouyun.advertisingsystem.config.EnvironmentConfig;
import com.sztouyun.advertisingsystem.mapper.AdvertisementMapper;
import com.sztouyun.advertisingsystem.model.advertisement.Advertisement;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementStatusEnum;
import com.sztouyun.advertisingsystem.model.advertisement.QAdvertisement;
import com.sztouyun.advertisingsystem.model.contract.QContractStore;
import com.sztouyun.advertisingsystem.model.mongodb.profit.AdvertisementProfitConfig;
import com.sztouyun.advertisingsystem.model.mongodb.profit.AdvertisementProfitModeEnum;
import com.sztouyun.advertisingsystem.model.mongodb.profit.AdvertisementStoreDailyProfit;
import com.sztouyun.advertisingsystem.model.mongodb.profit.StoreDailyProfit;
import com.sztouyun.advertisingsystem.model.monitor.QAdvertisementDailyDeliveryMonitorStatistic;
import com.sztouyun.advertisingsystem.model.store.QStoreInfo;
import com.sztouyun.advertisingsystem.model.store.StoreInfo;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementRepository;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementSettlementRepository;
import com.sztouyun.advertisingsystem.repository.contract.ContractStoreRepository;
import com.sztouyun.advertisingsystem.repository.monitor.AdvertisementDailyDeliveryMonitorStatisticRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.service.advertisement.AdvertisementService;
import com.sztouyun.advertisingsystem.service.common.StoreMultiThreadPageTaskService;
import com.sztouyun.advertisingsystem.service.store.StoreService;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.ShareAmountInfo;
import lombok.experimental.var;
import org.apache.calcite.linq4j.Linq4j;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * 广告收益计算
 */
@Service
public class AdvertisementProfitJobService extends BaseService {
    @Autowired
    private AdvertisementRepository advertisementRepository;
    @Autowired
    private AdvertisementSettlementRepository advertisementSettlementRepository;
    @Autowired
    private ContractStoreRepository contractStoreRepository;
    @Autowired
    private AdvertisementMapper advertisementMapper;
    @Autowired
    private StoreService storeService;
    @Autowired
    private AdvertisementService advertisementService;
    @Autowired
    private AdvertisementDailyDeliveryMonitorStatisticRepository advertisementDailyDeliveryMonitorStatisticRepository;
    @Autowired
    private StoreMultiThreadPageTaskService storeMultiThreadPageTaskService;
    @Autowired
    private EnvironmentConfig environmentConfig;
    @Autowired
    private MongoTemplate mongoTemplate;
    private QAdvertisement qAdvertisement = QAdvertisement.advertisement;
    private QContractStore qContractStore = QContractStore.contractStore;
    private QStoreInfo qStoreInfo = QStoreInfo.storeInfo;
    private final QAdvertisementDailyDeliveryMonitorStatistic qAdvertisementDailyDeliveryMonitorStatistic = QAdvertisementDailyDeliveryMonitorStatistic.advertisementDailyDeliveryMonitorStatistic;

    @Transactional
    public void resetAdvertisementShareProfit(List<String> advertisementIds){
        var filter = new BooleanBuilder(qAdvertisement.enableProfitShare.eq(true).and(qAdvertisement.mode.in(AdvertisementProfitModeEnum.DeliveryPoint.getValue(),AdvertisementProfitModeEnum.ActiveDegree.getValue())));
        var advertisements = advertisementRepository.findAll(qAdvertisement.id.in(advertisementIds).and(filter));
        advertisements.forEach(advertisement -> {
            //清空广告的分成金额和已结算金额
            advertisement.setShareAmount(0L);
            advertisement.setSettledAmount(0L);
            advertisementRepository.save(advertisement);
            //清空广告结算记录
            advertisementSettlementRepository.deleteByAdvertisementId(advertisement.getId());
            //清空广告门店日收益
            Query query= new Query(Criteria.where("advertisementId").in(advertisement.getId()));
            mongoTemplate.remove(query, AdvertisementStoreDailyProfit.class);
        });
    }

    /***
     * 重算广告日收益收益
     * @param date
     * @param advertisementIds
     */
    public void reCalculateAdvertisementDailyProfit(Date date,List<String> advertisementIds,boolean active,int orderCount,int bootHour) {
        var advertisements = getAdvertisements(date,advertisementIds);
        calculateAdvertisementDailyProfit(date,advertisements,active,orderCount,bootHour);
    }

    public void calculateAdvertisementDailyProfit(Date date) {
        var advertisements = getAdvertisements(date,null);
        calculateAdvertisementDailyProfit(date,advertisements,false,0,0);
    }

    private void calculateAdvertisementDailyProfit(Date date,Iterable<Advertisement> advertisements,boolean active,int orderCount,int bootHour) {
        //计算广告门店日收益金额
        calculateAdvertisementStoreDailyProfit(date,advertisements,active,orderCount,bootHour);
        //更新广告的分成金额
        updateAdvertisementShareAmount(Linq4j.asEnumerable(advertisements).select(a->a.getId()).toList(),date);
        //计算门店日收益金额
        //calculateStoreDailyProfit(date);
    }

    /**
     * 计算广告门店日收益记录
     */
    private void calculateAdvertisementStoreDailyProfit(Date date,Iterable<Advertisement> advertisements,boolean active,int orderCount,int bootHour){
        Date profitDate = new LocalDate(date).toDate();
        int pageSize = 500;
        for (var advertisement:advertisements){
            var advertisementProfitConfig =advertisementService.getAdvertisementProfitConfig(advertisement.getId());
            if(advertisementProfitConfig == null)
                continue;
            //初始化配置
            advertisementProfitConfig.init();
            Function<Integer,List<StoreInfo>> getPageListTask = pageIndex-> contractStoreRepository.findAll(q->
                    q.select(qContractStore.storeInfo).from(qContractStore).where(qContractStore.contractId.eq(advertisement.getContractId()))
                    .orderBy(qContractStore.storeId.desc()).offset(pageIndex*pageSize).limit(pageSize));

            Consumer<List<StoreInfo>> task = storeInfos->{
                try {
                    var activeStoreIds = active?null:getActiveStoreIds(advertisement.getId(),profitDate, Linq4j.asEnumerable(storeInfos).select(s -> s.getId()).toList());
                    var threadPool = new ThreadPool<StoreInfo,AdvertisementStoreDailyProfit>(storeInfos,8);
                    threadPool.setTask(storeInfo->generateAdvertisementStoreDailyProfit(advertisementProfitConfig,storeInfo, profitDate,active ||  activeStoreIds.contains(storeInfo.getId()),orderCount,bootHour));
                    var advertisementStoreDailyProfitList = threadPool.invokeAll();
                    advertisementStoreDailyProfitList.sort(Comparator.comparing(AdvertisementStoreDailyProfit::getStoreId).reversed());
                    mongoTemplate.insertAll(advertisementStoreDailyProfitList);
                }catch (Exception ex){
                    logger.error("生成广告日收益记录失败,advertisementId:"+advertisement.getId(),ex);
                }
            };
            var total = contractStoreRepository.count(qContractStore.contractId.eq(advertisement.getContractId()));
            storeMultiThreadPageTaskService.runPageTask(task,getPageListTask,total,1,pageSize);
        }
    }

    private AdvertisementStoreDailyProfit generateAdvertisementStoreDailyProfit(AdvertisementProfitConfig advertisementProfitConfig, StoreInfo storeInfo,Date date,boolean active,int orderCount,int bootHour){
        var storeId = storeInfo.getId();
        try {
            if(advertisementProfitConfig ==null)
                return null;
            AdvertisementProfitModeEnum advertisementProfitModeEnum = advertisementProfitConfig.getAdvertisementProfitModeEnum();
            if(advertisementProfitModeEnum ==null || advertisementProfitModeEnum.equals(AdvertisementProfitModeEnum.DeliveryEffect))
                return null;

            var advertisementStoreDailyProfit = new AdvertisementStoreDailyProfit(advertisementProfitConfig.getId(),storeId,date.getTime());
            advertisementStoreDailyProfit.setActive(active);
            long bootTime =bootHour*3600000L;
            if(orderCount ==0 && bootHour ==0){
                var storeDailyStatistic = storeService.getStoreDailyStatistic(storeId,date);
                if(storeDailyStatistic !=null){
                    orderCount = storeDailyStatistic.getOrderCount();
                    bootTime = storeDailyStatistic.getOpeningTimeDuration();
                }
            }
            advertisementStoreDailyProfit.setOrderCount(orderCount);
            advertisementStoreDailyProfit.setBootTime(bootTime);
            var bootTimeHourStandard = advertisementProfitConfig.getBootTimeStandard(storeInfo);
            advertisementStoreDailyProfit.setBootTimeStandard(bootTimeHourStandard*3600000L);
            advertisementStoreDailyProfit.calcIsQualified();
            if(advertisementStoreDailyProfit.isQualified()){
                advertisementStoreDailyProfit.setShareAmount(advertisementProfitConfig.getShareAmount(storeInfo,orderCount));
            }
            return advertisementStoreDailyProfit;
        }catch (Exception ex){
            logger.error("生成广告门店日收益记录失败,advertisementId:"+advertisementProfitConfig.getId()+"门店ID:"+storeId,ex);
        }
        return null;
    }

    private Iterable<Advertisement> getAdvertisements(Date date,List<String> advertisementIds){
        var nextDate = new DateTime(date).plusDays(1).toDate();
        var filter = new BooleanBuilder(qAdvertisement.enableProfitShare.eq(true).and(qAdvertisement.mode.in(AdvertisementProfitModeEnum.DeliveryPoint.getValue(),AdvertisementProfitModeEnum.ActiveDegree.getValue())));
        if(!CollectionUtils.isEmpty(advertisementIds)){
            filter.and(qAdvertisement.id.in(advertisementIds));
        }
        var finishedFilter =new BooleanBuilder(qAdvertisement.advertisementStatus.in(AdvertisementStatusEnum.TakeOff.getValue(),AdvertisementStatusEnum.Finished.getValue())
                .and(qAdvertisement.effectiveStartTime.lt(nextDate)
                        .and(qAdvertisement.effectiveEndTime.gt(date))));
        var deliveringFilter =new BooleanBuilder (qAdvertisement.advertisementStatus.eq(AdvertisementStatusEnum.Delivering.getValue())
                .and(qAdvertisement.effectiveStartTime.lt(nextDate)));
        return advertisementRepository.findAll(filter.and(finishedFilter.or(deliveringFilter)));
    }

    private List<String> getActiveStoreIds(String advertisementId,Date date, Collection<String> storeIds) {
        return advertisementDailyDeliveryMonitorStatisticRepository
                .findAll(q -> q.select(qAdvertisementDailyDeliveryMonitorStatistic.storeId).from(qAdvertisementDailyDeliveryMonitorStatistic)
                        .where(qAdvertisementDailyDeliveryMonitorStatistic.date.eq(date).and(qAdvertisementDailyDeliveryMonitorStatistic.storeId.in(storeIds)
                                .and(qAdvertisementDailyDeliveryMonitorStatistic.advertisementId.eq(advertisementId))
                        .and(qAdvertisementDailyDeliveryMonitorStatistic.displayTimes.gt(0)))).groupBy(qAdvertisementDailyDeliveryMonitorStatistic.storeId)
                );
    }

    private void calculateStoreDailyProfit(Date date) {
        Consumer<List<String>> task = storeIds->{
            try {
                var storeShareAmountMap = getStoreShareAmount(storeIds,date);
                List<StoreDailyProfit> storeDailyProfits = Linq4j.asEnumerable(storeIds).select(storeId->{
                    var storeDailyProfit = new StoreDailyProfit(storeId,date.getTime());
                    storeDailyProfit.setShareAmount(storeShareAmountMap.getOrDefault(storeId,0L));
                    return storeDailyProfit;
                }).toList();
                mongoTemplate.insertAll(storeDailyProfits);
            }catch (Exception ex){
                logger.error("生成门店日收益记录失败",ex);
            }
        };
        storeMultiThreadPageTaskService.runPageStoreIdTask(new BooleanBuilder(qStoreInfo.deleted.eq(false).or(qStoreInfo.updatedTime.gt(date))),task,5,500);
    }

    private Map<String,Long> getStoreShareAmount(List<String> storeIds, Date date){
        Aggregation agg = newAggregation(
                match(Criteria.where("storeId").in(storeIds)),
                match(Criteria.where("date").is(date.getTime())),
                match(Criteria.where("shareAmount").gt(0)),
                group("storeId")
                        .sum("shareAmount").as("shareAmount"),
                project("storeId","shareAmount")
        );
        var list= mongoTemplate.aggregate(agg, AdvertisementStoreDailyProfit.class, ShareAmountInfo.class).getMappedResults();
        return Linq4j.asEnumerable(list).toMap(s->s.getId(),s->s.getShareAmount());
    }

    private void updateAdvertisementShareAmount(Collection<String> advertisementIds,Date date){
        Aggregation agg = newAggregation(
                match(Criteria.where("advertisementId").in(advertisementIds)),
                match(Criteria.where("date").is(date.getTime())),
                match(Criteria.where("shareAmount").gt(0)),
                group("advertisementId")
                      .sum("shareAmount").as("shareAmount"),
                project("advertisementId","shareAmount")
                );
        var advertisementShareAmountInfos = mongoTemplate.aggregate(agg, AdvertisementStoreDailyProfit.class, ShareAmountInfo.class).getMappedResults();
        if(CollectionUtils.isEmpty(advertisementShareAmountInfos))
            return;

        advertisementMapper.updateAdvertisementShareAmount(advertisementShareAmountInfos);
    }
}
