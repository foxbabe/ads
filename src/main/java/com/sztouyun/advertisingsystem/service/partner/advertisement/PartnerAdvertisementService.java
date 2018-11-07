package com.sztouyun.advertisingsystem.service.partner.advertisement;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.sztouyun.advertisingsystem.common.jpa.JoinDescriptor;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.mapper.PartnerAdvertisementMapper;
import com.sztouyun.advertisingsystem.model.common.AreaLevelEnum;
import com.sztouyun.advertisingsystem.model.mongodb.PartnerAdvertisementDeliveryRecord;
import com.sztouyun.advertisingsystem.model.mongodb.PartnerAdvertisementOperationLog;
import com.sztouyun.advertisingsystem.model.monitor.QPartnerDailyStoreMonitorStatistic;
import com.sztouyun.advertisingsystem.model.partner.advertisement.PartnerAdvertisement;
import com.sztouyun.advertisingsystem.model.partner.advertisement.PartnerAdvertisementStatusEnum;
import com.sztouyun.advertisingsystem.model.partner.advertisement.QPartnerAdvertisement;
import com.sztouyun.advertisingsystem.model.partner.advertisement.store.PartnerAdvertisementDeliveryRecordStatusEnum;
import com.sztouyun.advertisingsystem.model.system.Area;
import com.sztouyun.advertisingsystem.model.system.QArea;
import com.sztouyun.advertisingsystem.repository.monitor.PartnerDailyStoreMonitorStatisticRepository;
import com.sztouyun.advertisingsystem.repository.partner.advertisement.PartnerAdvertisementRepository;
import com.sztouyun.advertisingsystem.repository.store.StoreInfoRepository;
import com.sztouyun.advertisingsystem.repository.system.AreaRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.service.partner.AdvertisementMediation.AdvertisementMediationEnum;
import com.sztouyun.advertisingsystem.service.partner.AdvertisementMediation.IAdvertisementMediationService;
import com.sztouyun.advertisingsystem.service.partner.CooperationPartnerService;
import com.sztouyun.advertisingsystem.service.store.StoreService;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.utils.SpringUtil;
import com.sztouyun.advertisingsystem.utils.UUIDUtils;
import com.sztouyun.advertisingsystem.utils.mongo.MongodbUtil;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.coorperationPartner.*;
import com.sztouyun.advertisingsystem.viewmodel.coorperationPartner.PartnerAdvertisementDisplayInfo;
import com.sztouyun.advertisingsystem.viewmodel.monitor.PartnerAdvertisementMonitorCountStatisticInfo;
import com.sztouyun.advertisingsystem.viewmodel.monitor.PartnerAdvertisementMonitorCountStatisticRequest;
import com.sztouyun.advertisingsystem.viewmodel.monitor.PartnerAdvertisementMonitorListRequest;
import com.sztouyun.advertisingsystem.viewmodel.partner.*;
import com.sztouyun.advertisingsystem.viewmodel.partner.store.StoreInfoRequest;
import com.sztouyun.advertisingsystem.viewmodel.store.PartnerAdvertisementRequestInfo;
import com.sztouyun.advertisingsystem.viewmodel.store.StorePartnerAdvertisementRequest;
import com.sztouyun.advertisingsystem.viewmodel.store.WrappedPartnerAdvertisementRequestInfo;
import lombok.experimental.var;
import org.apache.calcite.linq4j.Linq4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.mapreduce.MapReduceOptions;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
@CacheConfig(cacheNames = "partners")
public class PartnerAdvertisementService extends BaseService {

    @Autowired
    private PartnerAdvertisementMapper partnerAdvertisementMapper;
    @Autowired
    private PartnerAdvertisementRepository partnerAdvertisementRepository;
    @Autowired
    private StoreService storeService;
    @Autowired
    private AreaRepository areaRepository;
    @Autowired
    private SendPartnerAdvertisementDeliveryRecordService sendPartnerAdvertisementsService;
    @Autowired
    private PartnerAdvertisementDeliveryRecordService partnerAdvertisementDeliveryRecordService;
    @Autowired
    private CooperationPartnerService cooperationPartnerService;
    @Autowired
    private StoreInfoRepository storeInfoRepository;
    private final static QArea qArea=QArea.area;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private PartnerDailyStoreMonitorStatisticRepository partnerDailyStoreMonitorStatisticRepository;

    private final static QPartnerDailyStoreMonitorStatistic qPartnerDailyStoreMonitorStatistic = QPartnerDailyStoreMonitorStatistic.partnerDailyStoreMonitorStatistic;

    @Value("${partner.advertisement.local.monitor.url}")
    private String adsMonitorUrl;

    private static final QPartnerAdvertisement qPartnerAdvertisement = QPartnerAdvertisement.partnerAdvertisement;

    public Page<PartnerAdvertisementListViewModel> queryPartnerAdvertisementList(PartnerAdvertisementListRequest request) {
        request.setAuthenticationSql(partnerAdvertisementMapper.getUserAuthenticationFilterSql());
        List<PartnerAdvertisementListViewModel> viewModels = partnerAdvertisementMapper.queryPartnerAdvertisementList(request);
        List<String> ids=Linq4j.asEnumerable(viewModels).select(a -> a.getId()).toList();
        Map<String,PartnerAdvertisementDisplayInfo> map=getPartnerAdvertisementStatisticInfo(ids);
        viewModels.forEach(viewModel -> {
            if(map.containsKey(viewModel.getId())){
                PartnerAdvertisementDisplayInfo info=map.get(viewModel.getId());
                viewModel.setStoreCount(info.getStoreCount());
                viewModel.setRequestTimes(info.getRequestTimes());
                viewModel.setDisplayTimes(info.getDisplayTimes());
                viewModel.setValidDisplayTimes(info.getValidTimes());
            }
        });
        return pageResult(viewModels,new MyPageRequest(request.getPageIndex(), request.getPageSize()),partnerAdvertisementMapper.queryPartnerAdvertisementListCount(request));
    }

    public Map<String,PartnerAdvertisementDisplayInfo> getPartnerAdvertisementStatisticInfo(List<String> partnerAdvertisementIds){
        MapReduceOptions mro=new MapReduceOptions().outputTypeInline();
        mro.finalizeFunction("classpath:script/partnerAdvertisement/finalize.js");
        MapReduceResults<WrappedPartnerAdvertisementRequestAndDeliveryInfo> mapReduceResults=mongoTemplate.mapReduce(new Query(Criteria.where("partnerAdvertisementId").in(partnerAdvertisementIds)),
                "partnerAdvertisementDeliveryRecord",
                "classpath:script/partnerAdvertisement/mapFunc.js",
                "classpath:script/partnerAdvertisement/reduceFunc.js",
                mro,
                WrappedPartnerAdvertisementRequestAndDeliveryInfo.class
        );
        Map<String,PartnerAdvertisementDisplayInfo> map=new HashMap<>();
        for (WrappedPartnerAdvertisementRequestAndDeliveryInfo item:mapReduceResults){
            map.put(item.getId(),item.getValue());
        }
        return map;
    }

    public PartnerAdvertisementCountStatisticInfo getPartnerAdvertisementStatusStatistics(PartnerAdvertisementCountStatisticRequest request) {
        request.setAuthenticationSql(partnerAdvertisementMapper.getUserAuthenticationFilterSql());
        return partnerAdvertisementMapper.getPartnerAdvertisementStatusStatistics(request);
    }

    @Transactional
    @CacheEvict(key = "'TakeOffPartnerAdvertisementIds'")
    public void takeOff(String partnerAdvertisementId, String remark) {
        PartnerAdvertisement partnerAdvertisement = partnerAdvertisementRepository.findOne(partnerAdvertisementId);
        if(partnerAdvertisement==null)
            throw new BusinessException("广告不存在");
        if(!partnerAdvertisement.getAdvertisementStatus().equals(PartnerAdvertisementStatusEnum.Delivering.getValue()))
            throw new BusinessException("非投放中的广告不能进行下架操作");
        partnerAdvertisement.setAdvertisementStatus(PartnerAdvertisementStatusEnum.TakeOff.getValue());
        partnerAdvertisementRepository.save(partnerAdvertisement);
        var log = new PartnerAdvertisementOperationLog(partnerAdvertisementId, PartnerAdvertisementStatusEnum.TakeOff.getValue(),getUser().getId(),remark);
        mongoTemplate.save(log);
        Query query= new Query(Criteria.where("partnerAdvertisementId").is(partnerAdvertisementId));
        partnerAdvertisementDeliveryRecordService.takeOff(query,"广告下架，导致所有投放门店下的单个广告自动下架",getUser().getId());
    }

    public PartnerAdvertisement getPartnerAdvertisement(String PartnerAdvertisementId) {
        PartnerAdvertisement partnerAdvertisement = partnerAdvertisementRepository.findOneAuthorized(qPartnerAdvertisement.id.eq(PartnerAdvertisementId), new JoinDescriptor().innerJoin(qPartnerAdvertisement.partner));
        if (null == partnerAdvertisement)
            throw new BusinessException("第三方广告不存在或权限不足！");
        return partnerAdvertisement;
    }


    public List<PartnerAdvertisementOperationLog> queryPartnerAdvertisementOperationLogs(PartnerAdvertisementOperationLogsRequest request) {
        List<AggregationOperation> list=getLogCriteriaList(request).stream().map(item-> match(item)).collect(Collectors.toList());
        list.add(sort(Sort.Direction.DESC,"createdTime"));
        list.add(skip(request.getPageIndex()*request.getPageSize()));
        list.add(limit(request.getPageSize()));
        Aggregation agg = newAggregation(list);
        return mongoTemplate.aggregate(agg, "partnerAdvertisementOperationLog", PartnerAdvertisementOperationLog.class).getMappedResults();
    }

    private List<Criteria> getLogCriteriaList(PartnerAdvertisementOperationLogsRequest request){
        var criteria = new ArrayList<Criteria>();
        criteria.add(Criteria.where("partnerAdvertisementId").is(request.getId()));
        if(request.getOperationStatus() !=null && request.getOperationStatus()>0){
            criteria.add(Criteria.where("advertisementStatus").is(request.getOperationStatus()));
        }
        if(request.getStartTime() !=null && request.getEndTime() == null){
            criteria.add(Criteria.where("createdTime").gte(request.getStartTime().getTime()));
        }
        if(request.getEndTime() !=null && request.getStartTime() == null){
            criteria.add(Criteria.where("createdTime").lte(request.getEndTime().getTime()));
        }
        if(request.getEndTime() !=null && request.getStartTime() !=null) {//同一个字段(比如"createdTime")的Criteria不能连续加两次, 有上下区间必须写到一起
            criteria.add(Criteria.where("createdTime").gte(request.getStartTime().getTime()).lte(request.getEndTime().getTime()));
        }
        return criteria;
    }

    public Long queryPartnerAdvertisementOperationLogCount(PartnerAdvertisementOperationLogsRequest request) {
        Query query=new Query();
        getLogCriteriaList(request).stream().forEach(item-> query.addCriteria(item));
        return mongoTemplate.count(query, "partnerAdvertisementOperationLog");
    }

    public Map<String,PartnerAdvertisementRelatedInfo> getPartnerAdvertisementRelatedInfo(List<String> partnerIds) {
        if(partnerIds==null||partnerIds.size()==0)
            return new HashMap<>();
        NumberExpression<Long> deliveringCaseWhen = new CaseBuilder().when(qPartnerAdvertisement.advertisementStatus.eq(PartnerAdvertisementStatusEnum.Delivering.getValue())).then(1L).otherwise(0L);
        List<PartnerAdvertisementRelatedInfo> infos = partnerAdvertisementRepository.findAll(q -> q.select(Projections.bean(PartnerAdvertisementRelatedInfo.class,
                qPartnerAdvertisement.partnerId,
                qPartnerAdvertisement.id.count().as("totalDeliveryQuantity"),
                deliveringCaseWhen.sum().as("deliveryQuantity")
                ))
                        .from(qPartnerAdvertisement).where(qPartnerAdvertisement.partnerId.in(partnerIds))
                        .groupBy(qPartnerAdvertisement.partnerId)
        );
        return  Linq4j.asEnumerable(infos).toMap(a->a.getPartnerId(),b->b);
    }

    public List<Area> getPartnerAdvertisementStoreArea(String partnerAdvertisementId) {
        List<String> list=new ArrayList<>();
        EnumUtils.getAllItems(AreaLevelEnum.class).forEach(a->{
            list.addAll(getAreaIds(partnerAdvertisementId,a));
        });
        return  areaRepository.findAll(q->q.selectFrom(qArea).where(qArea.id.in(list)));
    }

    public List<String> getAreaIds(String partnerAdvertisementId,AreaLevelEnum areaLevelEnum){
        String pre="store.";
        String areaName=pre+getAreaName(areaLevelEnum);
        Aggregation aggregation=newAggregation(match(Criteria.where("partnerAdvertisementId").is(partnerAdvertisementId)),
                lookup("storeInfo","storeId","_id","store"),
                unwind("store"),
                group(areaName),
                project().and(areaName).as("id"));
        List<IdResult> ids = mongoTemplate.aggregate(aggregation,PartnerAdvertisementDeliveryRecord.class,IdResult.class).getMappedResults();
        return Linq4j.asEnumerable(ids).select(r->r.getId()).toList();
    }

    private String getAreaName(AreaLevelEnum areaLevelEnum){
        switch (areaLevelEnum){
            case Province:
                return "provinceId";
            case City:
                return "cityId";
            default:
                return "regionId";
        }
    }

    public Page<PartnerAdvertisementListViewModel> queryPartnerAdvertisementMonitors(PartnerAdvertisementMonitorListRequest request) {
        request.setAuthenticationSql(partnerAdvertisementMapper.getUserAuthenticationFilterSql());
        List<PartnerAdvertisementListViewModel> viewModels = partnerAdvertisementMapper.queryPartnerAdvertisementMonitors(request);
        Map<String,PartnerAdvertisementDisplayInfo> map=getPartnerAdvertisementStatisticInfo(Linq4j.asEnumerable(viewModels).select(a -> a.getId()).toList());
        viewModels.forEach(e -> {
            if(map.containsKey(e.getId())){
                PartnerAdvertisementDisplayInfo info=map.get(e.getId());
                e.setRequestTimes(info.getRequestTimes());
                e.setDisplayTimes(info.getDisplayTimes());
                e.setValidDisplayTimes(info.getValidTimes());
                e.setStoreCount(info.getStoreCount());
            }
        });
        return pageResult(viewModels,new MyPageRequest(request.getPageIndex(), request.getPageSize()),partnerAdvertisementMapper.queryPartnerAdvertisementMonitorsCount(request));
    }

    public PartnerAdvertisementMonitorCountStatisticInfo getPartnerAdvertisementMonitorStatusStatistics(PartnerAdvertisementMonitorCountStatisticRequest request) {
        request.setAuthenticationSql(partnerAdvertisementMapper.getUserAuthenticationFilterSql());
        return partnerAdvertisementMapper.getPartnerAdvertisementMonitorStatusStatistics(request);
    }

    public List<PartnerAdvertisementDeliveryRecord> requestPartnerAdvertisement(StoreInfoRequest storeInfoRequest) {
        List<PartnerAdvertisementDeliveryRecord> result = new ArrayList<>();
        if (!validateStore(storeInfoRequest))
            return result;

        IAdvertisementMediationService advertisementMediationService = AdvertisementMediationEnum.WaterFall.getAdvertisementMediationService();
        List<PartnerAdvertisementDeliveryRecord> partnerAdvertisements = advertisementMediationService.getPartnerAdvertisements(storeInfoRequest);
        if(CollectionUtils.isEmpty(partnerAdvertisements))
            return result;

        List<String> takeOffAdvertisementIds = SpringUtil.getBean(this.getClass()).getTakeOffPartnerAdvertisementIds();
        result.addAll(Linq4j.asEnumerable(partnerAdvertisements).where(a -> !takeOffAdvertisementIds.contains(a.getPartnerAdvertisementId())).toList());
        for(PartnerAdvertisementDeliveryRecord record : result) {
            record.setMonitorUrls(record.getMonitorUrls() + "," +adsMonitorUrl+record.getId());

            StringBuffer startPlayMonitorUrlBuffer = new StringBuffer();
            if(StringUtils.isEmpty(record.getStartDisplayMonitorUrls())) {
                startPlayMonitorUrlBuffer.append(adsMonitorUrl + record.getId() + "?action=" + PartnerAdvertisementTrackEnum.StartPlay.getValue());
            } else {
                startPlayMonitorUrlBuffer.append(record.getStartDisplayMonitorUrls()).append(",").append(adsMonitorUrl + record.getId() + "?action=" + PartnerAdvertisementTrackEnum.StartPlay.getValue());
            }
            record.setStartDisplayMonitorUrls(startPlayMonitorUrlBuffer.toString());
            sendPartnerAdvertisementsService.sendPartnerAdvertisementInfo(record);//放入消息队列
        }
        return result;
    }

    private Boolean validateStore(StoreInfoRequest storeInfoRequest) {
        if(!storeInfoRequest.getConnectionType().equals(ConnectionTypeEnum.WIFI.getValue())) {
            return Boolean.FALSE;
        }
        String storeId = storeService.getStoreIdByStoreNo(storeInfoRequest.getStoreNo());
        if(StringUtils.isEmpty(storeId))
            return false;

        storeInfoRequest.setStoreId(storeId);
        return Boolean.TRUE;
    }


    public List<String> getDeliveringAdvertisementIds(Collection<String> allPartnerAdvertisementIds){
        if(CollectionUtils.isEmpty(allPartnerAdvertisementIds))
            return new ArrayList<>();

        Aggregation agg = newAggregation(
                match(Criteria.where("partnerAdvertisementId").in(allPartnerAdvertisementIds)),
                match(Criteria.where("status").is(PartnerAdvertisementDeliveryRecordStatusEnum.Delivering.getValue())),
                group("partnerAdvertisementId"),
                project("partnerAdvertisementId")
        );
        List<IdResult> deliveringAdvertisementIds = mongoTemplate.aggregate(agg,PartnerAdvertisementDeliveryRecord.class,IdResult.class).getMappedResults();
        return Linq4j.asEnumerable(deliveringAdvertisementIds).select(r->r.getId()).toList();
    }

    public void  finishPartnerAdvertisement(List<String> finishedAdvertisementIds){
        if(!CollectionUtils.isEmpty(finishedAdvertisementIds)){
            partnerAdvertisementMapper.finishPartnerAdvertisement(finishedAdvertisementIds);
            List<PartnerAdvertisementOperationLog> partnerAdvertisementOperationLogList = Linq4j.asEnumerable(finishedAdvertisementIds).select(id->
                    new PartnerAdvertisementOperationLog(id,PartnerAdvertisementStatusEnum.Finished.getValue())).toList();
            //插入三方广告已完成的操作日志
            mongoTemplate.insertAll(partnerAdvertisementOperationLogList);
        }
    }

    @Cacheable(key = "'TakeOffPartnerAdvertisementIds'")
    public List<String> getTakeOffPartnerAdvertisementIds(){
        return partnerAdvertisementRepository.findAll(q->q.select(qPartnerAdvertisement.id).from(qPartnerAdvertisement).where(qPartnerAdvertisement.advertisementStatus.eq(PartnerAdvertisementStatusEnum.TakeOff.getValue())));
    }

    public List<PartnerAdvertisement> getPartnerAdvertisementList(List<String> ids){
        return partnerAdvertisementRepository.findAll(q->q.selectFrom(qPartnerAdvertisement).innerJoin(qPartnerAdvertisement.partner).
                where(qPartnerAdvertisement.id.in(ids)).fetchJoin());
    }

    public Page<PartnerAdvertisementRequestInfo> getPartnerAdvertisementRequestInfoPages(StorePartnerAdvertisementRequest request){
        List<String> partnerIds=cooperationPartnerService.getPartnerIds(request.getPartnerId(),request.getCooperationPattern());
        if(CollectionUtils.isEmpty(partnerIds) )
            return null;
        Pageable pageable=new MyPageRequest(request.getPageIndex(),request.getPageSize());
        request.setTempCollectionName(UUIDUtils.generateBase58UUID());
        request.setPartnerIds(partnerIds);
        calcPartnerAdvertisementRequestInfo(request);
        List<PartnerAdvertisementRequestInfo> partnerAdvertisementRequestInfos= queryPartnerAdvertisementRequestInfo(request);
        Long count=queryPartnerAdvertisementRequestInfoCount(request);
        Page<PartnerAdvertisementRequestInfo> pages= pageResult(partnerAdvertisementRequestInfos,pageable,count);
        new Thread(()->{
            mongoTemplate.dropCollection(request.getTempCollectionName());
        }).start();
        return pages;
    }

    private void calcPartnerAdvertisementRequestInfo(StorePartnerAdvertisementRequest request){
        MapReduceOptions mrO=new MapReduceOptions();
        mrO.outputCollection(request.getTempCollectionName());
        Query query=new Query();
        query.addCriteria(Criteria.where("storeId").is(request.getId()));
        query.addCriteria(Criteria.where("partnerId").in(request.getPartnerIds()));
        if(!org.springframework.util.StringUtils.isEmpty(request.getThirdPartId())){
            query.addCriteria(Criteria.where("thirdPartId").regex(request.getThirdPartId()));
        }
        mongoTemplate.mapReduce(query,
                "partnerAdvertisementDeliveryRecord",
                "classpath:script/partnerStoreAdvertisement/mapFunc.js",
                "classpath:script/partnerStoreAdvertisement/reduceFunc.js",
                mrO,
                WrappedPartnerAdvertisementRequestInfo.class
        );
    }

    private List<PartnerAdvertisementRequestInfo> queryPartnerAdvertisementRequestInfo(StorePartnerAdvertisementRequest request){
        List<AggregationOperation> operations = getAggregationOperations(request);
        operations.add(project()
                .and("value.partnerAdvertisementId").as("partnerAdvertisementId")
                .and("value.thirdPartId").as("thirdPartId")
                .and("value.requestTimes").as("requestTimes")
                .and("value.displayTimes").as("displayTimes")
                .and("value.validTimes").as("validTimes")
                .and("value.advertisementStatus").as("advertisementStatus")
                .and("value.materialType").as("materialType")
                .and("value.updateTime").as("updateTime"));
        operations.add(sort(Sort.Direction.DESC,"updateTime"));
        operations.add(skip(request.getPageSize()*request.getPageIndex()));
        operations.add(limit(request.getPageSize()));
        Aggregation aggregation=newAggregation(operations);
        return mongoTemplate.aggregate(aggregation,request.getTempCollectionName(),PartnerAdvertisementRequestInfo.class).getMappedResults();
    }

    private List<AggregationOperation> getAggregationOperations(StorePartnerAdvertisementRequest request) {
        List<AggregationOperation> operations=new ArrayList();
        if(!CollectionUtils.isEmpty(request.getPartnerIds())){
            operations.add(match(Criteria.where("value.partnerId").in(request.getPartnerIds())));
        }
        if(!org.springframework.util.StringUtils.isEmpty(request.getThirdPartId())){
            operations.add(match(Criteria.where("value.thirdPartId").regex(request.getThirdPartId())));
        }
        buildStatusOperation(request, operations);
        if(request.getMaterialType()!=null){
            operations.add(match(Criteria.where("value.materialType").is(request.getMaterialType())));
        }
        return operations;
    }

    private void buildStatusOperation(StorePartnerAdvertisementRequest request, List<AggregationOperation> operations) {
        List<Integer> statusList=request.getPartnerAdvertisementStatus();
        var takeOffPartnerAdvertisementIds = getTakeOffPartnerAdvertisementIds();
        Criteria takeOffCriteria=new Criteria();
        if(CollectionUtils.isEmpty(takeOffPartnerAdvertisementIds)){
            takeOffPartnerAdvertisementIds=new ArrayList<>();
        }
        takeOffCriteria.andOperator( Criteria.where("value.partnerAdvertisementId").in(takeOffPartnerAdvertisementIds));
        if(statusList.size()!=3){
            if(statusList.size()==1){
                PartnerAdvertisementStatusEnum statusEnum= EnumUtils.toEnum(statusList.get(0),PartnerAdvertisementStatusEnum.class);
                switch (statusEnum){
                    case Delivering:
                        operations.add(match(Criteria.where("value.advertisementStatus").is(PartnerAdvertisementStatusEnum.Delivering.getValue())));
                        break;
                    case TakeOff:
                        operations.add(match(takeOffCriteria));
                        break;
                    case Finished:
                        operations.add(match(Criteria.where("value.advertisementStatus").is(PartnerAdvertisementStatusEnum.Finished.getValue())));
                        if(!CollectionUtils.isEmpty(takeOffPartnerAdvertisementIds)){
                            operations.add(match(Criteria.where("value.partnerAdvertisementId").nin(takeOffPartnerAdvertisementIds)));
                        }
                        break;
                    default:
                        break;

                }
            }else if(!statusList.contains(PartnerAdvertisementStatusEnum.Delivering.getValue())){
                operations.add(match(Criteria.where("value.advertisementStatus").ne(PartnerAdvertisementStatusEnum.Delivering.getValue())));
            }else if(!statusList.contains(PartnerAdvertisementStatusEnum.TakeOff.getValue())){
                if(!CollectionUtils.isEmpty(takeOffPartnerAdvertisementIds)){
                    operations.add(match(Criteria.where("value.partnerAdvertisementId").nin(takeOffPartnerAdvertisementIds)));
                }
            }else{
                operations.add(
                        match(new Criteria().orOperator(
                                Criteria.where("value.advertisementStatus").is(PartnerAdvertisementStatusEnum.Delivering.getValue())
                                ,takeOffCriteria)
                        ));
            }
        }
    }

    private Long queryPartnerAdvertisementRequestInfoCount(StorePartnerAdvertisementRequest request){
        List<AggregationOperation> operations = getAggregationOperations(request);
        operations.add(count().as("count"));
        operations.add(project("count"));
        Aggregation aggregation=newAggregation(operations);
        return MongodbUtil.getCount(mongoTemplate,aggregation,request.getTempCollectionName(),Long.class);
    }

    private List<IdResult> getStorePartnerIds(String storeId){
        List<AggregationOperation> operations=new ArrayList<>();
        operations.add(match(Criteria.where("storeId").is(storeId)));
        operations.add(group("partnerId"));
        operations.add(project().and("partnerId").as("id"));
        Aggregation aggregation=newAggregation(operations);
        return mongoTemplate.aggregate(aggregation,PartnerAdvertisementDeliveryRecord.class,IdResult.class).getMappedResults();
    }

    public List<BasePartnerInfo> queryBasePartnerInfo(String storeId){
        if(!storeInfoRepository.exists(storeId))
            throw new BusinessException("门店ID无效");
        List<IdResult> idResults=getStorePartnerIds(storeId);
        if(idResults==null || idResults.isEmpty())
            return new ArrayList<>();
        return partnerAdvertisementMapper.getStorePartners(idResults.stream().map(a->a.getId()).collect(Collectors.toList()));
    }

    public Page<PartnerProfitStreamViewModel> profitStream(PartnerProfitStreamRequestViewModel request) {
        BooleanBuilder booleanBuilder = new BooleanBuilder().and(qPartnerDailyStoreMonitorStatistic.partnerId.eq(request.getPartnerId()));
        if (request.getStartTime() != null) {
            booleanBuilder.and(qPartnerDailyStoreMonitorStatistic.date.goe(request.getStartTime()));
        }
        if (request.getEndTime() != null) {
            booleanBuilder.and(qPartnerDailyStoreMonitorStatistic.date.loe(request.getEndTime()));
        }
        return partnerDailyStoreMonitorStatisticRepository.findAll(q -> q.select(
                Projections.bean(PartnerProfitStreamViewModel.class,
                        qPartnerDailyStoreMonitorStatistic.validTimes.longValue().sum().as("validTimes"),
                        qPartnerDailyStoreMonitorStatistic.profitAmount.sum().as("profitAmountDigit"),
                        qPartnerDailyStoreMonitorStatistic.date,
                        qPartnerDailyStoreMonitorStatistic.partnerId
                )).from(qPartnerDailyStoreMonitorStatistic)
                .where(booleanBuilder).groupBy(qPartnerDailyStoreMonitorStatistic.date).orderBy(qPartnerDailyStoreMonitorStatistic.date.desc()),
                new MyPageRequest(request.getPageIndex(), request.getPageSize()));
    }


    public Page<PartnerAdProfitViewModel> getAdvertisementPositionProfitStatistic(PartnerProfitStreamRequestViewModel request) {
        return partnerDailyStoreMonitorStatisticRepository.findAll(q -> q.select(
                Projections.bean(PartnerAdProfitViewModel.class,
                        qPartnerDailyStoreMonitorStatistic.validTimes.longValue().sum().as("validTimes"),
                        qPartnerDailyStoreMonitorStatistic.profitAmount.sum().as("profitAmountDigit"),
                        qPartnerDailyStoreMonitorStatistic.advertisementPositionCategory
                )).from(qPartnerDailyStoreMonitorStatistic).where((qPartnerDailyStoreMonitorStatistic.partnerId.eq(request.getPartnerId())))
                       .groupBy(qPartnerDailyStoreMonitorStatistic.advertisementPositionCategory),
                new MyPageRequest(request.getPageIndex(), request.getPageSize()));

    }
}
