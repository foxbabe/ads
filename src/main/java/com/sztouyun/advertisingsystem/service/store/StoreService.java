package com.sztouyun.advertisingsystem.service.store;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.mapper.StoreDailyStatisticMapper;
import com.sztouyun.advertisingsystem.mapper.StoreInfoMapper;
import com.sztouyun.advertisingsystem.model.contract.QContract;
import com.sztouyun.advertisingsystem.model.contract.QContractStore;
import com.sztouyun.advertisingsystem.model.mongodb.profit.StoreDailyProfit;
import com.sztouyun.advertisingsystem.model.store.*;
import com.sztouyun.advertisingsystem.repository.contract.ContractRepository;
import com.sztouyun.advertisingsystem.repository.contract.ContractStoreRepository;
import com.sztouyun.advertisingsystem.repository.store.StoreDailyStatisticRepository;
import com.sztouyun.advertisingsystem.repository.store.StoreInfoRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.AdvertisementAreaCountInfo;
import com.sztouyun.advertisingsystem.viewmodel.contract.ContractStoreInfo;
import com.sztouyun.advertisingsystem.viewmodel.internal.store.StoreDailyProfitViewModel;
import com.sztouyun.advertisingsystem.viewmodel.internal.store.UpdateStoreInfoRequest;
import com.sztouyun.advertisingsystem.viewmodel.monitor.AdvertisementStoreInfoStatisticViewModel;
import com.sztouyun.advertisingsystem.viewmodel.store.*;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
@CacheConfig(cacheNames = "stores")
public class StoreService extends BaseService {
    private final QContract qContract = QContract.contract;
    private final QStoreInfo qStoreInfo = QStoreInfo.storeInfo;
    private final QContractStore qContractStore = QContractStore.contractStore;
    private final QStoreDailyStatistic qStoreDailyStatistic = QStoreDailyStatistic.storeDailyStatistic;

    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private StoreInfoRepository storeInfoRepository;
    @Autowired
    private ContractStoreRepository contractStoreRepository;
    @Autowired
    private StoreDailyStatisticRepository storeDailyStatisticRepository;

    @Autowired
    private StoreInfoMapper storeInfoMapper;
    @Autowired
    private StoreDailyStatisticMapper storeDailyStatisticMapper;
    @Autowired
    private MongoTemplate mongoTemplate;

    public Page<StoreInfoQueryResult> findStoreListByArea(StoreInfoQueryRequest storeInfoQueryRequest, Pageable pageable) {
        if (!contractRepository.exists(qContract.id.eq(storeInfoQueryRequest.getContractId())))
            throw new BusinessException("合同Id不存在");

        return pageResult(storeInfoMapper.getStoreInfo(storeInfoQueryRequest), pageable, storeInfoMapper.getStoreInfoCount(storeInfoQueryRequest));
    }

    public Page<StoreInfoQueryResult> findStoreListByArea(CustomerStoreInfoQueryRequest storeInfoQueryRequest, Pageable pageable) {
        return pageResult(storeInfoMapper.getCustomerStoreInfo(storeInfoQueryRequest), pageable, storeInfoMapper.getCustomerStoreInfoCount(storeInfoQueryRequest));
    }


    public Page<StoreInfoStatisticViewModel> getStoreInfoStatistic(StoreInfoStatisticQueryRequest request, Pageable pageable) {
        return pageResult(storeInfoMapper.getStoreInfoStatistic(request), pageable, storeInfoMapper.getStoreInfoStatisticCount(request));
    }

    @Transactional
    public int chooseStoreIdTop(OneKeyInsertStoreInfoRequest insertStoreInfoRequest) {
        if (!contractRepository.exists(qContract.id.eq(insertStoreInfoRequest.getContractId())))
            throw new BusinessException("合同Id不存在");
        Long insertCount = storeInfoMapper.oneKeyInsertStoreToContract(insertStoreInfoRequest);
        if (insertCount > 0) {
            //重算下面所有门店的占用量
            storeInfoMapper.recalculateContractStoreUsedCount(insertStoreInfoRequest.getContractId());
        }
        return insertCount.intValue();
    }

    public int getChooseStoresCount(String contractId, int storeType) {
        return (int) contractStoreRepository.count(qContractStore.contractId.eq(contractId)
                .and(qContractStore.storeType.eq(storeType))
                .and(qContractStore.storeInfo.deleted.eq(false)));
    }

    public Page<AdvertisementAreaCountInfo> getAdvertisementAreaCountInfoList(String contractId, Pageable pageable) {
        Page<AdvertisementAreaCountInfo> page = contractStoreRepository.findAll(q -> q
                .select(Projections.bean(AdvertisementAreaCountInfo.class, qContractStore.storeInfo.cityId.as("cityId"), qContractStore.storeId.count().as("storeCount")))
                .from(qContractStore).leftJoin(qContractStore.storeInfo)
                .where(qContractStore.contractId.eq(contractId))
                .groupBy(qContractStore.storeInfo.cityId), pageable);
        return page;
    }

    public Page<ContractStoreInfo> queryStoreList(ContractStoreQueryRequest contractStoreQueryRequest, Pageable pageable) {
        if (!contractRepository.exists(qContract.id.eq(contractStoreQueryRequest.getContractId())))
            throw new BusinessException("合同ID无效");
        return pageResult(storeInfoMapper.queryStoreList(contractStoreQueryRequest), pageable, storeInfoMapper.queryStoreListCount(contractStoreQueryRequest));
    }

    public Page<StoreInfo> getContractStoreInfos(String contractId, Pageable pageable) {
        if (!contractRepository.exists(qContract.id.eq(contractId)))
            throw new BusinessException("合同ID无效");
        return contractStoreRepository.findAll(q -> q
                .select(qContractStore.storeInfo).from(qContractStore).innerJoin(qContractStore.storeInfo)
                .where(qContractStore.contractId.eq(contractId)), pageable);
    }

    public StoreAdvertisementInfoViewModel getStoreInfo(String id) {
        if (!storeInfoRepository.exists(qStoreInfo.id.eq(id).and(qStoreInfo.deleted.eq(false))))
            throw new BusinessException("门店ID无效");
        return storeInfoMapper.getStoreAdvertisementInfo(id);
    }

    public Long storeInfoUseCountStatistic() {
        return storeInfoMapper.storeInfoUseCountStatistic();
    }

    public int getAllStoreInfoCount() {
        return Long.valueOf(storeInfoRepository.count(qStoreInfo.deleted.isFalse().and(qStoreInfo.storeType.gt(0)))).intValue();
    }

    public Long getAllStoreCountByStoreType(Integer storeType, String contractId) {
        return storeInfoMapper.getTotalStoreCount(storeType, contractId);
    }


    public Long getStoreCountInAbnormal(Integer storeType) {
        return storeInfoRepository.count(inAbnormalStoreFilter(storeType));
    }

    private BooleanBuilder inAbnormalStoreFilter(Integer storeType) {
        BooleanBuilder pre = new BooleanBuilder(qStoreInfo.deleted.isFalse().and(qStoreInfo.provinceId.eq("").or(qStoreInfo.cityId.eq("").or(qStoreInfo.regionId.eq(""))))).and(qStoreInfo.isTest.isFalse());
        if (storeType == null) {
            pre.and(qStoreInfo.storeType.gt(0));
        } else {
            pre.and(qStoreInfo.storeType.eq(storeType));
        }
        return pre;
    }

    public Long getTestStoreCount(Integer storeType) {
        return storeInfoRepository.count(testStoreFilter(storeType));
    }

    private BooleanBuilder testStoreFilter(Integer storeType) {
        BooleanBuilder pre = new BooleanBuilder(qStoreInfo.deleted.isFalse().and(qStoreInfo.isTest.isTrue()));
        if (storeType == null) {
            pre.and(qStoreInfo.storeType.gt(0));
        } else {
            pre.and(qStoreInfo.storeType.eq(storeType));
        }
        return pre;
    }

    public List<String> getAvailableStoreIds(List<String> storeIds) {
        return storeInfoRepository.findAll(q -> q.select(qStoreInfo.id).from(qStoreInfo).where(qStoreInfo.available.isTrue().and(qStoreInfo.id.in(storeIds))));
    }

    @Transactional
    public void moveStore(UpdateStoreInfoRequest request) {
        StoreInfo storeInfo = storeInfoRepository.findOne(qStoreInfo.storeNo.eq(request.getOldStoreNo()).and(qStoreInfo.storeSource.eq(StoreSourceEnum.OMS.getValue())));
        if (storeInfo == null)
            throw new BusinessException("旧门店编号不存在");
        if (storeInfoRepository.exists(qStoreInfo.storeNo.eq(request.getNewStoreNo())))
            throw new BusinessException("新门店编号已存在");
        storeInfo.setStoreNo(request.getNewStoreNo());
        storeInfo.setStoreSource(StoreSourceEnum.NEW_OMS.getValue());
        storeInfo.setDeleted(false);
        storeInfoRepository.save(storeInfo);
    }

    @Transactional
    public void replaceStore(UpdateStoreInfoRequest request) {
        StoreInfo storeInfo = storeInfoRepository.findOne(qStoreInfo.storeNo.eq(request.getOldStoreNo()));
        if (storeInfo == null)
            throw new BusinessException("旧门店编号不存在");
        if (storeInfoRepository.exists(qStoreInfo.storeNo.eq(request.getNewStoreNo())))
            throw new BusinessException("新门店编号已存在");
        storeInfo.setStoreNo(request.getNewStoreNo());
        storeInfo.setStoreSource(StoreSourceEnum.NEW_OMS.getValue());
        storeInfo.setDeleted(false);
        storeInfoRepository.save(storeInfo);
    }

    public PrimaryStoreInfoViewModel queryStoreInfo(PrimaryStoreInfoRequest request) {
        return storeInfoMapper.queryStoreInfo(request);
    }

    public List<StoreInfo> queryStores(String address, Integer limit) {
        return storeInfoRepository.findAll(q -> q.select(qStoreInfo).from(qStoreInfo).where(qStoreInfo.storeAddress.contains(address).and(qStoreInfo.deleted.isFalse())).orderBy(qStoreInfo.id.desc()).limit(limit.longValue()));
    }

    @Cacheable(key = "#p0",condition="#p0!=null")
    public String getStoreIdByStoreNo(String storeNo) {
        return storeInfoRepository.findOne(q->q.select(qStoreInfo.id).from(qStoreInfo).where(qStoreInfo.storeNo.eq(storeNo)));
    }

    public List<StorePlacementViewModel> getStoreInfoByCoordinate(StoreInfoQueryRequest info) {
        return storeInfoMapper.getStoreInfoByCoordinate(info);
    }

    public List<CustomerStorePlacementViewModel> getCustomerStoreInfoByCoordinate(CustomerStoreInfoQueryRequest info) {
        return storeInfoMapper.getCustomerStoreInfoByCoordinate(info);
    }

    public Long getAllAvailableStoreCount(String customerStorePlanID) {
        return storeInfoMapper.getAllAvailableStoreCount(customerStorePlanID);
    }

    public Long getStoreCountInAbnormal(String customerStorePlanID) {
        return storeInfoMapper.getStoreCountInAbnormal(customerStorePlanID);
    }

    public List<StoreInfoAreaStatistic> getStoreInfoUsedCountStatisticByCity(StoreAreaStatisticRequest request){
        return storeInfoMapper.getStoreInfoUsedCountStatisticByCity(request);
    }

    public AdvertisementStoreInfoStatisticViewModel getAdvertisementStatisticByStoreId(String storeId) {
        if (!storeInfoRepository.exists(storeId))
            throw new BusinessException("门店ID无效");
        return storeInfoMapper.getAdvertisementStatisticByStoreId(storeId);
    }

    public int getStoreCount(Date date){
        date = new LocalDate(date).toDate();
        if(LocalDate.now().toDate().getTime()==date.getTime())
            return storeInfoRepository.getCountByDeleted(false);
        return (int) storeDailyStatisticRepository.count(qStoreDailyStatistic.date.eq(date));
    }

    public Map<Date, StoreNumStatistics> getStoreNumStatistics(StoreAdFillTrendRequest request) {
        return storeDailyStatisticMapper.getStoreNumStatistics(request).stream().collect(Collectors.toMap(StoreNumStatistics::getDate, e -> e));
    }

    @Cacheable(key = "#p0+'_'+#p1.getTime()",condition="#p0!=null")
    public StoreDailyStatistic getStoreDailyStatistic(String storeId,Date date){
        return storeDailyStatisticRepository.findOne(qStoreDailyStatistic.date.eq(date).and(qStoreDailyStatistic.storeId.eq(storeId)));
    }

    public StoreNumStatistics getStoreNumStatisticsByNow() {
        return storeInfoMapper.getStoreNumStatisticsByNow();
    }

    public List<StoreDailyProfitViewModel> storeDailyProfitList(String storeId,Date startTime,Date endTime) {
        List<AggregationOperation> list=new ArrayList<>();
        list.add(match(Criteria.where("storeId").is(storeId)));
        list.add(match(Criteria.where("date").gte(startTime.getTime()).lte(endTime.getTime())));
        list.add(sort(Sort.Direction.ASC,"id"));
        return mongoTemplate.aggregate(newAggregation(list), StoreDailyProfit.class, StoreDailyProfitViewModel.class).getMappedResults();
    }
}
