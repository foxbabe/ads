package com.sztouyun.advertisingsystem.service.monitor;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.jpa.JoinDescriptor;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.mapper.AdvertisementDailyLinkMonitorStatisticMapper;
import com.sztouyun.advertisingsystem.mapper.AdvertisementStoreMonitorMapper;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementStatusEnum;
import com.sztouyun.advertisingsystem.model.advertisement.QAdvertisement;
import com.sztouyun.advertisingsystem.model.common.MonitorStatusEnum;
import com.sztouyun.advertisingsystem.model.contract.QContractStore;
import com.sztouyun.advertisingsystem.model.monitor.*;
import com.sztouyun.advertisingsystem.model.system.AdvertisementPositionCategoryEnum;
import com.sztouyun.advertisingsystem.model.system.Area;
import com.sztouyun.advertisingsystem.model.system.TerminalTypeEnum;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementRepository;
import com.sztouyun.advertisingsystem.repository.contract.ContractStoreRepository;
import com.sztouyun.advertisingsystem.repository.monitor.AdvertisementDailyDeliveryMonitorStatisticRepository;
import com.sztouyun.advertisingsystem.repository.monitor.AdvertisementMonitorStatisticRepository;
import com.sztouyun.advertisingsystem.repository.monitor.AdvertisingDailyStoreMonitorStatisticRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.service.system.AreaService;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.viewmodel.monitor.*;
import com.sztouyun.advertisingsystem.viewmodel.store.StoreInfoAreaSelectedViewModel;
import lombok.experimental.var;
import org.apache.calcite.linq4j.Linq4j;
import org.apache.calcite.linq4j.function.IntegerFunction1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.querydsl.QSort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * Created by wenfeng on 2017/10/31.
 */
@Service
public class AdvertisementMonitorStatisticService extends BaseService {
    @Autowired
    private AdvertisementMonitorStatisticRepository advertisementMonitorStatisticRepository;
    @Autowired
    private AdvertisementRepository advertisementRepository;
    @Autowired
    private AdvertisementStoreMonitorMapper advertisementStoreMonitorMapper;
    @Autowired
    private AreaService areaService;
    @Autowired
    private AdvertisingDailyStoreMonitorStatisticRepository dailyStoreMonitorStatisticRepository;
    @Autowired
    private ContractStoreRepository contractStoreRepository;
    @Autowired
    private AdvertisementDailyDeliveryMonitorStatisticRepository dailyDeliveryMonitorStatisticRepository;
    @Autowired
    private AdvertisementDailyLinkMonitorStatisticMapper advertisementDailyLinkMonitorStatisticMapper;
    @Autowired
    private MongoTemplate mongoTemplate;



    private final QAdvertisementMonitorStatistic qAdvertisementMonitorStatistic=QAdvertisementMonitorStatistic.advertisementMonitorStatistic;
    private final QAdvertisement qAdvertisement = QAdvertisement.advertisement;
    private final QAdvertisingDailyStoreMonitorStatistic qDailyStoreMonitorStatistic = QAdvertisingDailyStoreMonitorStatistic.advertisingDailyStoreMonitorStatistic;
    private final QContractStore qContractStore = QContractStore.contractStore;
    private final QAdvertisementDailyDeliveryMonitorStatistic qDailyDeliveryMonitorStatistic = QAdvertisementDailyDeliveryMonitorStatistic.advertisementDailyDeliveryMonitorStatistic;

    public AdvertisementMonitorStatistic getAdvertisementMonitorStatistic(ContractStoreInfoQueryRequest request){
        if(!advertisementRepository.existsAuthorized(qAdvertisement.id.eq(request.getAdvertisementId()))) {
            throw new BusinessException("广告ID无效");
        }
        return advertisementMonitorStatisticRepository.findOne(qAdvertisementMonitorStatistic.id.eq(request.getAdvertisementId()) ,new JoinDescriptor().innerJoin(qAdvertisementMonitorStatistic.advertisement));
    }

    public Page<AdvertisementMonitorStatistic> getAdvertisementMonitorStatistic(AdvertisementMonitorStatisticRequest request){

        Pageable pageable=new PageRequest(request.getPageIndex(),request.getPageSize(), new QSort(qAdvertisementMonitorStatistic.updatedTime.desc()));
        return advertisementMonitorStatisticRepository.findAllAuthorized(filter(request),pageable,new JoinDescriptor().innerJoin(qAdvertisementMonitorStatistic.advertisement).innerJoin(qAdvertisementMonitorStatistic.contract));
    }

    private BooleanBuilder filter(AdvertisementMonitorStatisticRequest request) {
        BooleanBuilder predicate = new BooleanBuilder();
        if (!StringUtils.isEmpty(request.getAdvertisementName())) {
            predicate = predicate.and(qAdvertisementMonitorStatistic.advertisement.advertisementName.contains(request.getAdvertisementName()));
        }
        if (!StringUtils.isEmpty(request.getContractName())) {
            predicate = predicate.and(qAdvertisementMonitorStatistic.advertisement.contract.contractName.contains(request.getContractName()));
        }
        if (!StringUtils.isEmpty(request.getCustomerName())) {
            predicate = predicate.and(qAdvertisementMonitorStatistic.advertisement.customer.name.contains(request.getCustomerName()));
        }
        if (!StringUtils.isEmpty(request.getNickname())) {
            predicate = predicate.and(qAdvertisementMonitorStatistic.contract.owner.nickname.contains(request.getNickname()));
        }
        if (request.getStartTime() != null) {
            predicate.and(qAdvertisementMonitorStatistic.createdTime.goe(request.getStartTime()));
        }
        if (request.getEndTime() != null) {
            predicate.and(qAdvertisementMonitorStatistic.createdTime.loe(request.getEndTime()));
        }
        if (request.getPlatform() != null && request.getPlatform() == 0) {
            predicate.and(qAdvertisementMonitorStatistic.contract.platform.eq(Constant.ALL_TERMINATE_TYPE));
        }
        if (request.getPlatform() != null && request.getPlatform() > 0) {
            predicate.and(qAdvertisementMonitorStatistic.contract.platform.contains(request.getPlatform().toString()+Constant.BACKSLASH)
                    .or(qAdvertisementMonitorStatistic.contract.platform.contains(Constant.BACKSLASH+request.getPlatform().toString()))
                    .or(qAdvertisementMonitorStatistic.contract.platform.contains(request.getPlatform().toString())));
        }
        if (request.getStatus() != 0) {
            MonitorStatusEnum monitorStatusEnum = EnumUtils.toEnum(request.getStatus(), MonitorStatusEnum.class);
            switch (monitorStatusEnum) {
                case Finished:
                    predicate.and(qAdvertisementMonitorStatistic.advertisement.advertisementStatus.in(AdvertisementStatusEnum.TakeOff.getValue(), AdvertisementStatusEnum.Finished.getValue()));
                    break;
                case OnWatching:
                    predicate.and(qAdvertisementMonitorStatistic.advertisement.advertisementStatus.eq(AdvertisementStatusEnum.Delivering.getValue()));
                    break;
            }
        }
        return predicate;
    }

    public List<MonitorStatusStatistic> queryAdvertisementStatusStatistics(AdvertisementMonitorStatisticRequest request) {
        request.setStatus(MonitorStatusEnum.All.getValue());
        return advertisementMonitorStatisticRepository.findAllAuthorized(q -> q.select(
                Projections.bean(MonitorStatusStatistic.class,
                    qAdvertisementMonitorStatistic.advertisement.advertisementStatus.as("status"),
                    qAdvertisementMonitorStatistic.id.count().as("count")))
                .from(qAdvertisementMonitorStatistic)
                .innerJoin(qAdvertisementMonitorStatistic.advertisement)
                .innerJoin(qAdvertisementMonitorStatistic.contract)
                .where(filter(request))
                .groupBy(qAdvertisementMonitorStatistic.advertisement.advertisementStatus));
    }

    public AdvertisementMonitorStatistic queryAdvertisementMonitorDetail(String advertisementId) {
        return advertisementMonitorStatisticRepository.findOneAuthorized(qAdvertisementMonitorStatistic.id.eq(advertisementId));
    }


    public Page<AdvertisementStoreMonitorItem> getMonitorStoreItems(AdvertisementStoreMonitorItemRequest request, Pageable pageable) {
        if(!advertisementRepository.existsAuthorized(qAdvertisement.id.eq(request.getAdvertisementId())))
            throw new BusinessException("广告ID不存在");
        return pageResult(advertisementStoreMonitorMapper.getMonitorStoreItems(request), pageable, advertisementStoreMonitorMapper.getMonitorStoreItemCount(request));
    }


    public List<Area> getStoreAreaInfo(String advertisementId) {
        String contractId = advertisementRepository.findOneAuthorized(q -> q.select(qAdvertisement.contractId).from(qAdvertisement).where(qAdvertisement.id.eq(advertisementId)));
        if(StringUtils.isEmpty(contractId)) {
            throw new BusinessException("广告数据不存在");
        }
        return areaService.getAreaInfo(new StoreInfoAreaSelectedViewModel(contractId));
    }

    /**
     * 获得广告的展示次数, 按日期, 广告位类型分组
     * @param displayTimesBrokenChartRequest
     * @return
     */
    public List<AdvertisementPositionDailyDisplayTimesStatistic> getDaliyDisplayTimesStatistics(DisplayTimesBrokenChartRequest displayTimesBrokenChartRequest) {
        return this.advertisementStoreMonitorMapper.getAdvertisementPositionDailyDisplayTimesStatistic(displayTimesBrokenChartRequest);
    }

    public List<AdvertisingDailyStoreMonitorStatistic> getActiveStoreGraphData(StoreGraphTimeSpanViewModel viewModel) {
        return dailyStoreMonitorStatisticRepository.findAllAuthorized(q -> q.selectFrom(qDailyStoreMonitorStatistic)
                .where(qDailyStoreMonitorStatistic.date.goe(viewModel.getStartDate())
                        .and(qDailyStoreMonitorStatistic.date.loe(viewModel.getEndDate()))
                        .and(qDailyStoreMonitorStatistic.advertisementId.eq(viewModel.getAdvertisementId()))
                ));
    }

    public Long getActiveStoreCountAtTime(String advertisementId, Date date) {
        return dailyDeliveryMonitorStatisticRepository.countAuthorized(q -> q.selectFrom(qDailyDeliveryMonitorStatistic)
                .where(qDailyDeliveryMonitorStatistic.advertisementId.eq(advertisementId)
                        .and(qDailyDeliveryMonitorStatistic.storeId.isNotEmpty())
                                .and(qDailyDeliveryMonitorStatistic.displayTimes.gt(0))
                                        .and(qDailyDeliveryMonitorStatistic.date.loe(date)))
                .groupBy(qDailyDeliveryMonitorStatistic.storeId));
    }

    public Long getAvailableStoreCount(String advertisementId) {
        String contractId = advertisementRepository.findOneAuthorized(q -> q.select(qAdvertisement.contractId).from(qAdvertisement).where(qAdvertisement.id.eq(advertisementId)));
        if(StringUtils.isEmpty(contractId)) {
            throw new BusinessException("广告数据不存在或者权限不足");
        }

        return contractStoreRepository.countAuthorized(q -> q.selectFrom(qContractStore).innerJoin(qContractStore.storeInfo)
                .where(qContractStore.contractId.eq(contractId).and(qContractStore.storeInfo.available.isTrue()).and(qContractStore.storeInfo.id.isNotNull())));
    }

    public Long getActiveStoreCount(String advertisementId) {
        if(!advertisementRepository.existsAuthorized(qAdvertisement.id.eq(advertisementId))) {
            throw new BusinessException("广告数据不存在或者权限不足");
        }

        return dailyDeliveryMonitorStatisticRepository.count(q -> q.select(qDailyDeliveryMonitorStatistic.storeId)
                .from(qDailyDeliveryMonitorStatistic)
                .where(qDailyDeliveryMonitorStatistic.advertisementId.eq(advertisementId)
                        .and(qDailyDeliveryMonitorStatistic.displayTimes.gt(0))
                        .and(qDailyDeliveryMonitorStatistic.storeId.isNotEmpty()))
                .groupBy(qDailyDeliveryMonitorStatistic.storeId));
    }

    /**
     * 获得所有的广告位对应的展示次数
     * @param advertisementId 广告ID
     * @return key:广告位类型, AdvertisementPositionCategoryEnum value:展示次数
     */
    public Map<Integer, Integer> getAllAdvertisementPositionCategoryDisplayTimes(String advertisementId) {
        HashMap<Integer, Integer> map = new HashMap<>();
        List<AdvertisementPositionDisplayTimes> list = getAdvertisementPositionDisplayTimes(advertisementId,null);
        list.forEach(a -> map.put(a.getAdvertisementPositionCategory(), a.getDisplayTimes()));
        return map;
    }

    public Map<Integer, Integer> getTerminalDisplayTimes(String advertisementId) {
        return getTerminalDisplayTimes(advertisementId,null);
    }

    public Map<Integer, Integer> getTerminalDisplayTimes(String advertisementId,List<TerminalTypeEnum> terminalTypeEnums) {
        HashMap<Integer, Integer> map = new HashMap<>();
        List<AdvertisementPositionDisplayTimes> list = getAdvertisementPositionDisplayTimes(advertisementId,terminalTypeEnums);
        if(list ==null || list.isEmpty())
            return map;
        return Linq4j.asEnumerable(list).groupBy(a->a.getTerminalType())
                .toMap(a->a.getKey(),a->a.sum((IntegerFunction1<AdvertisementPositionDisplayTimes>) d ->d.getDisplayTimes()));
    }

    private List<AdvertisementPositionDisplayTimes> getAdvertisementPositionDisplayTimes(String advertisementId,List<TerminalTypeEnum> terminalTypeEnums){
        return dailyDeliveryMonitorStatisticRepository.findAll(q->{
              var query = q.select(Projections.bean(AdvertisementPositionDisplayTimes.class,
                        qDailyDeliveryMonitorStatistic.advertisementPositionCategory,
                        qDailyDeliveryMonitorStatistic.displayTimes.sum().as("displayTimes")
                )).from(qDailyDeliveryMonitorStatistic);
                if(!StringUtils.isEmpty(advertisementId)){
                    query = query.where(qDailyDeliveryMonitorStatistic.advertisementId.eq(advertisementId));
                }
                if(terminalTypeEnums !=null && !terminalTypeEnums.isEmpty()){
                    List<Integer> positionCategories = Linq4j.asEnumerable(EnumUtils.getAllItems(AdvertisementPositionCategoryEnum.class))
                            .where(a->terminalTypeEnums.contains(a.getTerminalType())).select(a->a.getValue()).toList();
                    query = query.where(qDailyDeliveryMonitorStatistic.advertisementPositionCategory.in(positionCategories));
                }
                query = query.groupBy(qDailyDeliveryMonitorStatistic.advertisementPositionCategory);
                return query;
                });
    }

    public List<StoreParticipationStatistics> getAllStoreParticipationStatistics(String advertisementId) {
        return advertisementDailyLinkMonitorStatisticMapper.getAllStoreParticipationStatistics(advertisementId);
    }

    public List<ClickOrScanTimes> getAllClickOrScanTimes(String advertisementId) {
        return advertisementDailyLinkMonitorStatisticMapper.getAllClickOrScanTimes(advertisementId);
    }

    public List<DateLinkTimes> getAllLinkTimes(ClickOrScanTimesTrendRequest request) {
        return advertisementDailyLinkMonitorStatisticMapper.getAllLinkTimes(request);
    }

    public  Map<String,Integer> getPhoneCount(List<String> urlStepIds){
        Map<String,Integer> map=new HashMap<>();
        Aggregation aggregation = newAggregation(
                match(Criteria.where("urlStepId").in(urlStepIds)),
                match(Criteria.where("valid").is(Boolean.TRUE)),
                match(Criteria.where("phone").exists(Boolean.TRUE)),
                group("urlStepId").count().as("count")
        );
        Iterator<Map> iterator= mongoTemplate.aggregate(aggregation,"materialMonitorInfo", Map.class).iterator();
        while (iterator.hasNext()){
            Map temp=iterator.next();
            map.put(temp.get("_id").toString(),Integer.valueOf(temp.get("count").toString()));
        }
        return map;
    }

    public long getAdvertisementLinkPhoneCountByUrlStepIds(List<String> urlStepIds) {
        Query query=new Query();
        query.addCriteria(Criteria.where("urlStepId").in(urlStepIds));
        query.addCriteria(Criteria.where("valid").is(Boolean.TRUE));
        query.addCriteria(Criteria.where("phone").exists(Boolean.TRUE));
        return mongoTemplate.count(query, "materialMonitorInfo");
    }

    public List<StorePhoneInfo> getAdvertisementDailyLinkMonitorInfo(List<String> urlStepIds , Long skip, Long limit) {
        Aggregation agg = newAggregation(
                match(Criteria.where("urlStepId").in(urlStepIds)),
                match(Criteria.where("valid").is(Boolean.TRUE)),
                match(Criteria.where("phone").exists(Boolean.TRUE)),
                project("storeId","phone"),
                skip(skip),
                limit(limit)
        );
        return mongoTemplate.aggregate(agg, "materialMonitorInfo", StorePhoneInfo.class).getMappedResults();
    }

    public List<MaterialLinkMonitorInfo> getMaterialLinkMonitorInfo(String advertisementMaterialId , Integer linkType){
        return advertisementDailyLinkMonitorStatisticMapper.getMaterialLinkMonitorInfo(advertisementMaterialId,linkType);
    }
}
