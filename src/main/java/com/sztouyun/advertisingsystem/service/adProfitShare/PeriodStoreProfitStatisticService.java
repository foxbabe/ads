package com.sztouyun.advertisingsystem.service.adProfitShare;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.sztouyun.advertisingsystem.common.jpa.JoinDescriptor;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.mapper.PeriodStoreProfitStatisticMapper;
import com.sztouyun.advertisingsystem.mapper.StoreProfitStatisticMapper;
import com.sztouyun.advertisingsystem.model.adProfitShare.PeriodStoreProfitStatistic;
import com.sztouyun.advertisingsystem.model.adProfitShare.QPeriodStoreProfitStatistic;
import com.sztouyun.advertisingsystem.model.store.QStoreInfo;
import com.sztouyun.advertisingsystem.model.system.Area;
import com.sztouyun.advertisingsystem.repository.adProfitShare.PeriodStoreProfitStatisticRepository;
import com.sztouyun.advertisingsystem.repository.adProfitShare.SettledStoreProfitRepository;
import com.sztouyun.advertisingsystem.repository.store.StoreInfoRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.viewmodel.adProfitShare.AdvertisementPeriodStoreProfitStatisticRequest;
import com.sztouyun.advertisingsystem.viewmodel.adProfitShare.PeriodDetailItemRequestViewModel;
import com.sztouyun.advertisingsystem.viewmodel.adProfitShare.PeriodStoreProfitStatisticRequest;
import com.sztouyun.advertisingsystem.viewmodel.adProfitShare.StoreProfitStatisticWithMonthViewModel;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.store.ContractStoreQueryRequest;
import com.sztouyun.advertisingsystem.viewmodel.store.PeriodStoreProfitStatisticInfo;
import com.sztouyun.advertisingsystem.viewmodel.store.PeriodStoreProfitStatisticViewModel;
import com.sztouyun.advertisingsystem.viewmodel.store.StoreInfoMonthList;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QSort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Service
public class PeriodStoreProfitStatisticService extends BaseService {
    @Autowired
    private PeriodStoreProfitStatisticRepository periodStoreProfitStatisticRepository;
    @Autowired
    private SettledStoreProfitRepository settledStoreProfitRepository;
    @Autowired
    private StoreProfitStatisticMapper storeProfitStatisticMapper;
    @Autowired
    private StoreInfoRepository storeInfoRepository;
    private final QStoreInfo qStoreInfo = QStoreInfo.storeInfo;
    @Autowired
    private PeriodStoreProfitStatisticMapper periodStoreProfitStatisticMapper;

    private static final QPeriodStoreProfitStatistic qPeriodStoreProfitStatistic = QPeriodStoreProfitStatistic.periodStoreProfitStatistic;

    public Page<PeriodStoreProfitStatistic> getSettledStoreProfitStatisticById(PeriodStoreProfitStatisticRequest request) {
        if(!settledStoreProfitRepository.exists(request.getSettledStoreProfitId()))
            throw new BusinessException("无此结算记录");
        Pageable pageable = new MyPageRequest(request.getPageIndex(), request.getPageSize(),new QSort(qPeriodStoreProfitStatistic.settledMonth.desc()));
        return periodStoreProfitStatisticRepository.findAll(filter(request), pageable, new JoinDescriptor().leftJoin(qPeriodStoreProfitStatistic.storeInfo,qStoreInfo));
    }

    public Page<PeriodStoreProfitStatistic> getSettledStoreProfitStatistic(PeriodStoreProfitStatisticRequest request) {
        Pageable pageable = new MyPageRequest(request.getPageIndex(), request.getPageSize(), new QSort(qPeriodStoreProfitStatistic.settledMonth.desc()));
        return periodStoreProfitStatisticRepository.findAll(filter(request), pageable, new JoinDescriptor().innerJoin(qPeriodStoreProfitStatistic.storeInfo,qStoreInfo));
    }

    private BooleanBuilder filter(PeriodStoreProfitStatisticRequest request) {
        BooleanBuilder predicate = new BooleanBuilder(qPeriodStoreProfitStatistic.shareAmount.gt(0));
        if (!StringUtils.isEmpty(request.getSettledStoreProfitId())) {
            predicate.and(qPeriodStoreProfitStatistic.settledStoreProfitId.eq(request.getSettledStoreProfitId()));
        }
        if (!StringUtils.isEmpty(request.getStoreName())) {
            predicate.and(qStoreInfo.storeName.contains(request.getStoreName()));
        }
        if (!StringUtils.isEmpty(request.getDeviceId())) {
            predicate.and(qStoreInfo.deviceId.contains(request.getDeviceId()));
        }
        if(!StringUtils.isEmpty(request.getShopId())){
            predicate.and(qStoreInfo.storeNo.contains(request.getShopId()));
        }
        if(!StringUtils.isEmpty(request.getProvinceId())){
            predicate.and(qStoreInfo.provinceId.eq(request.getProvinceId()));
        }
        if(!StringUtils.isEmpty(request.getCityId())){
            predicate.and(qStoreInfo.cityId.eq(request.getCityId()));
        }
        if(!StringUtils.isEmpty(request.getRegionId())){
            predicate.and(qStoreInfo.regionId.eq(request.getRegionId()));
        }
        if (Objects.nonNull(request.getIsQualified())) {
            predicate.and(qStoreInfo.isQualified.eq(request.getIsQualified()));
        }
        if (request.getStartTime() != null) {
            predicate.and(qPeriodStoreProfitStatistic.settledMonth.goe(request.getStartTime()));
        }
        if (request.getEndTime() != null) {
            predicate.and(qPeriodStoreProfitStatistic.settledMonth.lt(new LocalDate(request.getEndTime()).plusMonths(1).toDate()));
        }
        if (request.getSettled() != null) {
            predicate.and(qPeriodStoreProfitStatistic.settled.eq(request.getSettled()));
        }
        return predicate;
    }

    public Double getTotalAmount(PeriodStoreProfitStatisticRequest request){
        return periodStoreProfitStatisticRepository.findOne(q->
                q.select(qPeriodStoreProfitStatistic.shareAmount.multiply(100).sum().divide(100)).from(qPeriodStoreProfitStatistic).innerJoin(qPeriodStoreProfitStatistic.storeInfo,qStoreInfo).where(filter(request)));
    }

    public List<PeriodStoreProfitStatisticInfo> getPeriodStoreProfitStatisticByContractId(PeriodStoreProfitStatisticViewModel periodStoreProfitStatisticViewModel){
        return periodStoreProfitStatisticMapper.getAdvertisementStoreProfitPeriodStatistic(periodStoreProfitStatisticViewModel);
    }

    public List<Area> getAreasBySettledStoreProfitId(String settledStoreProfitId) {
        return storeProfitStatisticMapper.getAreaBySettledStoreProfitId(settledStoreProfitId);
    }

    public StoreProfitStatisticWithMonthViewModel storeProfitStatisticWithMonth(String storeId) {

        if(!storeInfoRepository.exists(storeId))
            throw new BusinessException("门店数据不存在");

        NumberExpression<Double> settledCaseWhen = new CaseBuilder().when(qPeriodStoreProfitStatistic.settled.isTrue()).then(qPeriodStoreProfitStatistic.shareAmount).otherwise(0D);

        StoreProfitStatisticWithMonthViewModel result = periodStoreProfitStatisticRepository.findOne(q ->
                q.select(Projections.bean(StoreProfitStatisticWithMonthViewModel.class,
                        settledCaseWhen.multiply(100).sum().divide(100).as("settledAmount"),
                        qPeriodStoreProfitStatistic.shareAmount.multiply(100).sum().divide(100).as("totalShareAmount")))
                        .from(qPeriodStoreProfitStatistic)
                        .where(qPeriodStoreProfitStatistic.storeId.eq(storeId))
        );
        return result == null ? new StoreProfitStatisticWithMonthViewModel() : result;
    }



    public Page<PeriodStoreProfitStatistic> getPeriodDetailItem(PeriodDetailItemRequestViewModel request) {
        if(!storeInfoRepository.exists(request.getStoreId()))
            throw new BusinessException("门店数据不存在");

        Pageable pageable = new MyPageRequest(request.getPageIndex(), request.getPageSize(),new QSort(qPeriodStoreProfitStatistic.settledMonth.desc()));
        BooleanBuilder predicate = new BooleanBuilder().and(qPeriodStoreProfitStatistic.storeId.eq(request.getStoreId()));
        if (request.getSettled() != null) {
            predicate.and(qPeriodStoreProfitStatistic.settled.eq(request.getSettled()));
        }
        if (request.getStartDate() != null) {
            predicate.and(qPeriodStoreProfitStatistic.settledMonth.goe(request.getStartDate()));
        }
        if (request.getEndDate() != null) {
            predicate.and(qPeriodStoreProfitStatistic.settledMonth.loe(request.getEndDate()));
        }
        return periodStoreProfitStatisticRepository.findAll(predicate, pageable);
    }

    public List<StoreInfoMonthList> getStoreMonthProfitList(AdvertisementPeriodStoreProfitStatisticRequest request) {
        return periodStoreProfitStatisticMapper.getStoreMonthProfitList(request);
    }

    public List<StoreInfoMonthList> getAdvertisementStoreMonthProfitList(ContractStoreQueryRequest request) {
        return periodStoreProfitStatisticMapper.getAdvertisementStoreMonthProfitList(request);
    }
}
