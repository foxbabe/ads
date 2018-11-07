package com.sztouyun.advertisingsystem.service.partner;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.mapper.PartnerAdvertisementMapper;
import com.sztouyun.advertisingsystem.mapper.PartnerDailyStoreMonitorStatisticMapper;
import com.sztouyun.advertisingsystem.mapper.PartnerMediaConfigMapper;
import com.sztouyun.advertisingsystem.model.account.User;
import com.sztouyun.advertisingsystem.model.common.CodeTypeEnum;
import com.sztouyun.advertisingsystem.model.monitor.QPartnerDailyStoreMonitorStatistic;
import com.sztouyun.advertisingsystem.model.partner.*;
import com.sztouyun.advertisingsystem.model.partner.advertisement.store.QPartnerMediaConfig;
import com.sztouyun.advertisingsystem.repository.account.UserRepository;
import com.sztouyun.advertisingsystem.repository.monitor.PartnerDailyStoreMonitorStatisticRepository;
import com.sztouyun.advertisingsystem.repository.partner.CooperationPartnerRepository;
import com.sztouyun.advertisingsystem.repository.partner.PartnerAdSlotConfigRepository;
import com.sztouyun.advertisingsystem.repository.partner.advertisement.store.PartnerMediaConfigRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.service.common.CodeGenerationService;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.coorperationPartner.*;
import com.sztouyun.advertisingsystem.viewmodel.monitor.RequestStoreRankBaseInfo;
import com.sztouyun.advertisingsystem.viewmodel.monitor.RequestStoreRankInfo;
import com.sztouyun.advertisingsystem.viewmodel.partner.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapreduce.MapReduceOptions;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@CacheConfig(cacheNames = "partners")
public class CooperationPartnerService extends BaseService {

    @Autowired
    private CodeGenerationService codeGenerationService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CooperationPartnerRepository cooperationPartnerRepository;
    @Autowired
    private PartnerAdvertisementMapper partnerAdvertisementMapper;
    @Autowired
    private PartnerDailyStoreMonitorStatisticMapper partnerDailyStoreMonitorStatisticMapper;
    @Autowired
    private PartnerMediaConfigRepository partnerMediaConfigRepository;
    @Autowired
    private PartnerDailyStoreMonitorStatisticRepository partnerDailyStoreMonitorStatisticRepository;
    @Autowired
    private PartnerMediaConfigMapper partnerMediaConfigMapper;
    @Autowired
    private PartnerAdSlotConfigRepository partnerAdSlotConfigRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    private final QPartnerAdSlotConfig qPartnerAdSlotConfig = QPartnerAdSlotConfig.partnerAdSlotConfig;
    private final QCooperationPartner qCooperationPartner = QCooperationPartner.cooperationPartner;
    private final QPartnerMediaConfig qPartnerMediaConfig = QPartnerMediaConfig.partnerMediaConfig;
    private final QPartnerDailyStoreMonitorStatistic qPartnerDailyStoreMonitorStatistic = QPartnerDailyStoreMonitorStatistic.partnerDailyStoreMonitorStatistic;

    @Transactional
    public String createCooperationPartner(CooperationPartner cooperationPartner) {

        boolean exist = cooperationPartnerRepository.existsByName(cooperationPartner.getName());
        if(exist)
            throw new BusinessException("合作方名称已存在");

        String cooperationPartnerNumber = codeGenerationService.generateCode(CodeTypeEnum.PAR);
        cooperationPartner.setCode(cooperationPartnerNumber);
        String id = getUser().getId();
        cooperationPartner.setCreatorId(id);
        cooperationPartner.setOwnerId(id);
        cooperationPartner.setPriority(cooperationPartnerRepository.findOne(q->q.select(qCooperationPartner.priority.max().nullif(0)).from(qCooperationPartner))+1);
        return cooperationPartnerRepository.save(cooperationPartner).getId();
    }

    @Transactional
    public void updateCooperationPartner(String id, CooperationPartner cooperationPartner) {
        CooperationPartner existCooperationPartner = findCooperationPartnerById(id);
        if(existCooperationPartner==null)
            throw new BusinessException("合作方不存在");

        boolean exists = cooperationPartnerRepository.exists(qCooperationPartner.name.eq(cooperationPartner.getName()).and(qCooperationPartner.id.ne(id)));
        if(exists){
            throw new BusinessException("合作方名称已存在");
        }

        cooperationPartner.setId(id);
        cooperationPartner.setDisabled(existCooperationPartner.isDisabled());
        cooperationPartner.setOwnerId(existCooperationPartner.getOwnerId());
        cooperationPartner.setCode(existCooperationPartner.getCode());
        //非管理员不能修改ApiUrl
        if(!getUser().isAdmin()){
            cooperationPartner.setApiUrl(existCooperationPartner.getApiUrl());
        }
        cooperationPartner.setPriority(existCooperationPartner.getPriority());
        cooperationPartnerRepository.save(cooperationPartner);
    }

    @Transactional
    @CacheEvict(key = "'PrioritizedPartnerIds'")
    public void toggleEnabled(String id) {
        CooperationPartner cooperationPartner = findCooperationPartnerById(id);
        if (null == cooperationPartner)
            throw new BusinessException("合作方不存在");
        boolean currentStatus = cooperationPartner.isDisabled();
        cooperationPartner.setDisabled(!currentStatus);
        cooperationPartnerRepository.save(cooperationPartner);
    }

    public Page<CooperationPartner> queryCooperationPartnerList(CooperationPartnerBasePageInfoViewModel viewModel, Pageable pageable) {
        BooleanBuilder predicate =new BooleanBuilder();
        if(!StringUtils.isEmpty(viewModel.getName())){
            predicate.and(qCooperationPartner.name.contains(viewModel.getName()));
        }
        if(!StringUtils.isEmpty(viewModel.getCode())){
            predicate.and(qCooperationPartner.code.contains(viewModel.getCode()));
        }
        if(!StringUtils.isEmpty(viewModel.getNickname())){
            predicate.and(qCooperationPartner.owner.nickname.contains(viewModel.getNickname()));
        }
        if(viewModel.getCooperationPattern()!=null){
            predicate.and(qCooperationPartner.cooperationPattern.eq(viewModel.getCooperationPattern()));
        }
        return  cooperationPartnerRepository.findAllAuthorized(predicate,pageable);
    }

    public Page<CooperationPartnerConfigViewModel> queryCooperationPartnerConfigList(CooperationPartnerConfigRequest request) {

        return  cooperationPartnerRepository.findAll(q->q.select(Projections.bean(CooperationPartnerConfigViewModel.class,
                qCooperationPartner.id,
                qCooperationPartner.name,
                qCooperationPartner.code,
                qCooperationPartner.cooperationPattern,
                qCooperationPartner.priority,
                qCooperationPartner.disabled))
                .from(qCooperationPartner)
                .orderBy(qCooperationPartner.priority.asc()),
                new MyPageRequest(request.getPageIndex(), request.getPageSize()));
    }

    public boolean partnerIsEnabled(String partnerId){
        CooperationPartner partner = findCooperationPartnerById(partnerId);
        if(partner ==null)
            return false;
        return !partner.isDisabled();
    }

    public String getPartnerNameFromCache(String partnerId) {
        CooperationPartner partner = findCooperationPartnerById(partnerId);
        if(partner ==null)
            return "";
        return partner.getName();
    }

    public CooperationPartner findCooperationPartnerById(String partnerId) {
        CooperationPartner cooperationPartner = cooperationPartnerRepository.findById(partnerId);
        if (cooperationPartner == null) {
            throw new BusinessException("合作方信息不存在");
        }
        return cooperationPartner;
    }

    @Transactional
    public void distributeCooperationPartner(String cooperationPartnerId, String ownerId) {
        CooperationPartner cooperationPartner = cooperationPartnerRepository
            .findOne(cooperationPartnerId);
        if (null == cooperationPartner)
            throw new BusinessException("合作方不存在");
        User user = userRepository.findById(ownerId);
        if (null == user)
            throw new BusinessException("业务员不存在");
        cooperationPartner.setOwnerId(ownerId);
        cooperationPartnerRepository.save(cooperationPartner);
    }

    @Transactional
    @CacheEvict(key = "'PrioritizedPartnerIds'")
    public void updatePriority(String partnerId,Integer replacedPriority) {
        CooperationPartner cooperationPartner = cooperationPartnerRepository.findById(partnerId);
        if(cooperationPartner==null)
            throw new BusinessException("合作方不存在");
        if(!cooperationPartnerRepository.exists(qCooperationPartner.priority.eq(replacedPriority))||replacedPriority.equals(cooperationPartner.getPriority()))
            return;
        if(cooperationPartner.getPriority()>replacedPriority){
            partnerAdvertisementMapper.updatePriority(replacedPriority,cooperationPartner.getPriority(),1);//1:上移一层
        }else {
            partnerAdvertisementMapper.updatePriority(cooperationPartner.getPriority(),replacedPriority,-1);//-1:下移一层
        }
        cooperationPartner.setPriority(replacedPriority);
        cooperationPartnerRepository.save(cooperationPartner);
    }

    @Cacheable(key = "'PrioritizedPartnerIds'")
    public List<String> getPrioritizedPartnerIds(){
       return cooperationPartnerRepository.findAll(q->q.select(qCooperationPartner.id).from(qCooperationPartner).where(qCooperationPartner.disabled.eq(false)).orderBy(qCooperationPartner.priority.asc()));
    }

    public List<CooperationPartnerChartRequestTimesViewModel> partnerAdvertisementRequestLogStatistic(CooperationPartnerChartBaseRequest request) {
        return partnerAdvertisementMapper.partnerAdvertisementRequestLogStatistic(request);
    }

    public List<CooperationPartnerPieChartRequestTimesInfo> requestTimesPieChartStatistic(CooperationPartnerPieChartRequest request) {
        return partnerDailyStoreMonitorStatisticRepository.findAll(q -> q.select(Projections.bean(CooperationPartnerPieChartRequestTimesInfo.class,
                qPartnerDailyStoreMonitorStatistic.advertisementPositionCategory.as("advertisementPositionCategory"),
                qPartnerDailyStoreMonitorStatistic.apiErrorTimes.longValue().sum().as("apiErrorTimes"),
                qPartnerDailyStoreMonitorStatistic.getAdTimes.longValue().sum().as("requestSuccessTimes"),
                qPartnerDailyStoreMonitorStatistic.getNoAdTimes.longValue().sum().as("getNoAdTimes")))
                .from(qPartnerDailyStoreMonitorStatistic)
                .where(filter(request)).groupBy(qPartnerDailyStoreMonitorStatistic.advertisementPositionCategory));
    }

    private BooleanBuilder filter(CooperationPartnerPieChartRequest request) {
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(qPartnerDailyStoreMonitorStatistic.partnerId.eq(request.getCooperationPartnerId()));
        if (request.getStartTime() != null) {
            predicate.and(qPartnerDailyStoreMonitorStatistic.date.goe(request.getStartTime()));
        }
        if (request.getEndTime() != null) {
            predicate.and(qPartnerDailyStoreMonitorStatistic.date.loe(request.getEndTime()));
        }
        return predicate;
    }

    public List<AdPositionStatisticsTimes> getAdPositionStatisticsTimes(DisplayTimesProportionRequest request) {
        return partnerDailyStoreMonitorStatisticMapper.getAdPositionStatisticsTimes(request);
    }

    public CooperationPartnerLineChartStoreCountViewModel requestStoreCountLineChartStatistic(CooperationPartnerPieChartRequest request) {
        List<CooperationPartnerLineChartStoreCountInfo> list = partnerDailyStoreMonitorStatisticMapper.requestStoreCountLineChartStatistic(request);
        CooperationPartnerLineChartStoreCountViewModel viewModel = new CooperationPartnerLineChartStoreCountViewModel();
        if(list!=null&&list.size()>0){
            viewModel.setCooperationPartnerLineChartStoreCountInfos(list);
            viewModel.setConfigStoreCount(list.get(0).getConfigStoreCount());
        }else{
            long count = partnerMediaConfigRepository.count(qPartnerMediaConfig.partnerId.eq(request.getCooperationPartnerId()));
            viewModel.setConfigStoreCount(Long.valueOf(count).intValue());
        }
        Long periodRequestStoreCount = partnerDailyStoreMonitorStatisticRepository.findOne(q ->
                q.select(qPartnerDailyStoreMonitorStatistic.storeId.countDistinct())
                .from(qPartnerDailyStoreMonitorStatistic)
                .where(filter(request)));
        viewModel.setPeriodRequestStoreCount(periodRequestStoreCount==null?0:periodRequestStoreCount.intValue());

        return viewModel;
    }

    public List<DisplayTimesViewModel> getPartnerDisplayTimesStatistic(DisplayTimesRequest request) {
        return partnerDailyStoreMonitorStatisticMapper.getPartnerDisplayTimesStatistic(request);
    }

	public List<RequestStoreRankInfo> getRequestStoreRankInfo(StoreRankInfoRequest request){
        if(!cooperationPartnerRepository.exists(request.getPartnerId()))
            throw new BusinessException("合作方ID无效");
        return partnerDailyStoreMonitorStatisticMapper.getRequestStoreRankInfo(request);
    }

    public List<StoreRequestRankingViewModel> getAllPartnerStoreNum(StoreRequestRankingRequest request) {
        return partnerDailyStoreMonitorStatisticMapper.getAllPartnerStoreNum(request);
    }
    public List<String> getPartnerIds(String partnerId,Integer cooperationPattern){
        BooleanBuilder predicate = new BooleanBuilder();
        if(!StringUtils.isEmpty(partnerId)){
            predicate.and(qCooperationPartner.id.eq(partnerId));
        }
        if(cooperationPattern!=null){
            predicate.and(qCooperationPartner.cooperationPattern.eq(cooperationPattern));
        }
	    return cooperationPartnerRepository.findAll(q->q.select(qCooperationPartner.id).from(qCooperationPartner).where(predicate));
    }

    public Map<String,Long> findStoreCountByCity(String partnerId){
        List<RequestStoreRankBaseInfo> list= partnerMediaConfigMapper.findStoreCountByCity(partnerId);
        return list.stream().collect(Collectors.toMap(a->a.getCityId(),a->a.getConfigStoreCount()));
    }

    public List<PartnerAdSlotConfig> partnerAdSlotConfigList(String partnerId) {
        return partnerAdSlotConfigRepository.findAll(q->
                q.selectFrom(qPartnerAdSlotConfig)
                        .where(qPartnerAdSlotConfig.partnerId.eq(partnerId))
                        .orderBy(qPartnerAdSlotConfig.priority.asc()));
    }

    @Cacheable(key = "'PrioritizedAdSlots_'+#p0",condition="#p0!=null")
    public List<Integer> getPrioritizedAdSlots(String partnerId){
         return partnerAdSlotConfigRepository.findAll(q->
                q.select(qPartnerAdSlotConfig.adSlot).from(qPartnerAdSlotConfig)
                        .where(qPartnerAdSlotConfig.partnerId.eq(partnerId).and(qPartnerAdSlotConfig.enabled.eq(true)))
                        .orderBy(qPartnerAdSlotConfig.priority.asc()));
    }

    @Transactional
    public void toggleAdSlotEnabled(long partnerAdSlotConfigId) {
        PartnerAdSlotConfig partnerAdSlotConfig = getPartnerAdSlotConfigById(partnerAdSlotConfigId);
        boolean enabled = partnerAdSlotConfig.getEnabled();
        partnerAdSlotConfig.setEnabled(!enabled);
        partnerAdSlotConfig.setUpdaterId(getUser().getId());
        partnerAdSlotConfigRepository.save(partnerAdSlotConfig);
    }

    private PartnerAdSlotConfig getPartnerAdSlotConfigById(long partnerAdSlotConfigId) {
        PartnerAdSlotConfig partnerAdSlotConfig =partnerAdSlotConfigRepository.findOne(qPartnerAdSlotConfig.id.eq(partnerAdSlotConfigId));
        if (null == partnerAdSlotConfig)
            throw new BusinessException("合作方广告位配置不存在");
        return partnerAdSlotConfig;
    }

    @Transactional
    public void updateAdSlotPriority(long checkedId, Integer replacedPriority) {
        PartnerAdSlotConfig partnerAdSlotConfig = getPartnerAdSlotConfigById(checkedId);
        if(!partnerAdSlotConfigRepository.exists(qPartnerAdSlotConfig.priority.eq(replacedPriority))||replacedPriority.equals(partnerAdSlotConfig.getPriority()))
            return;
        if(partnerAdSlotConfig.getPriority()>replacedPriority){
            partnerAdvertisementMapper.updateAdSlotPriority(getUpdatedAdSlotPriorityInfo(replacedPriority,partnerAdSlotConfig.getPriority(),1));//1:上移一层
        }else {
            partnerAdvertisementMapper.updateAdSlotPriority(getUpdatedAdSlotPriorityInfo(partnerAdSlotConfig.getPriority(),replacedPriority,-1));//-1:下移一层
        }
        partnerAdSlotConfig.setPriority(replacedPriority);
        partnerAdSlotConfig.setUpdaterId(getUser().getId());
        partnerAdSlotConfigRepository.save(partnerAdSlotConfig);
    }

    private UpdatedAdSlotPriorityInfo getUpdatedAdSlotPriorityInfo(Integer startPriority,Integer endPriority,Integer movedStep) {
        UpdatedAdSlotPriorityInfo info = new UpdatedAdSlotPriorityInfo();
        info.setStartPriority(startPriority);
        info.setEndPriority(endPriority);
        info.setMovedStep(movedStep);
        info.setUpdaterId(getUser().getId());
        info.setUpdatedTime(new Date());
        return info;
    }

    public MapReduceResults<AdvertisementRequestCountResult> getAdvertisementRequestStatistic(String cooperationPartnerId,Date startTime,Date endTime) {
        MapReduceOptions mapReduceOptions = new MapReduceOptions();
        mapReduceOptions.outputTypeInline();
        mapReduceOptions.finalizeFunction("classpath:script/partnerAdvertisementStatistic/finalize.js");
        Query query = new Query();
        if(startTime!=null && endTime==null){
            query.addCriteria(Criteria.where("requestDate").gte(startTime.getTime()));
        }
        if(startTime==null && endTime!=null){
            query.addCriteria(Criteria.where("requestDate").lte(endTime.getTime()));
        }
        if(startTime!=null && endTime!=null) {
            query.addCriteria(Criteria.where("requestDate").gte(startTime.getTime()).lte(endTime.getTime()));
        }
        if (!StringUtils.isEmpty(cooperationPartnerId)){
            query.addCriteria(Criteria.where("partnerId").is(cooperationPartnerId));
        }
        return mongoTemplate.mapReduce(query,
                "partnerAdvertisementDeliveryRecord",
                "classpath:script/partnerAdvertisementStatistic/mapFunc.js",
                "classpath:script/partnerAdvertisementStatistic/reduceFunc.js",
                mapReduceOptions,
                AdvertisementRequestCountResult.class);
    }

    public MapReduceResults<AdvertisementRequestCountLineChartResult> getAdvertisementRequestLineChartStatistic(String cooperationPartnerId, Date startTime, Date endTime) {
        MapReduceOptions mapReduceOptions = new MapReduceOptions();
        mapReduceOptions.outputTypeInline();
        mapReduceOptions.finalizeFunction("classpath:script/partnerAdvertisementLineChartStatistic/finalize.js");

        return mongoTemplate.mapReduce(Query.query(Criteria.where("requestDate").gte(startTime.getTime()).lte(endTime.getTime())).addCriteria(Criteria.where("partnerId").is(cooperationPartnerId)),
                "partnerAdvertisementDeliveryRecord",
                "classpath:script/partnerAdvertisementLineChartStatistic/mapFunc.js",
                "classpath:script/partnerAdvertisementLineChartStatistic/reduceFunc.js",
                mapReduceOptions,
                AdvertisementRequestCountLineChartResult.class);
    }

    public CooperationPartnerProfitStatisticViewModel profitStatistic(String partnerId,Date yesterday, Date week, Date month) {
        return partnerDailyStoreMonitorStatisticMapper.profitStatistic(partnerId,yesterday,week,month);
    }

    public List<CooperationPartnerLineChartProfitTrendInfo> partnerProfitTrendLineChart(String cooperationPartnerId, Date startTime, Date endTime) {
        return partnerDailyStoreMonitorStatisticMapper.partnerProfitTrendLineChart(cooperationPartnerId,startTime,endTime);
    }
}
