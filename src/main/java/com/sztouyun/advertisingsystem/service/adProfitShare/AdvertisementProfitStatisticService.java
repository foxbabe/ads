package com.sztouyun.advertisingsystem.service.adProfitShare;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.jpa.JoinDescriptor;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.adProfitShare.AdvertisementProfitStatistic;
import com.sztouyun.advertisingsystem.model.adProfitShare.QAdvertisementProfitStatistic;
import com.sztouyun.advertisingsystem.model.adProfitShare.QAdvertisementStoreProfitPeriodStatistic;
import com.sztouyun.advertisingsystem.repository.adProfitShare.AdvertisementProfitStatisticRepository;
import com.sztouyun.advertisingsystem.repository.adProfitShare.AdvertisementStoreProfitPeriodStatisticRepository;
import com.sztouyun.advertisingsystem.repository.adProfitShare.StoreProfitStatisticRepository;
import com.sztouyun.advertisingsystem.repository.store.StoreInfoRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.viewmodel.adProfitShare.AdvertisementProfitSharePageInfoRequest;
import com.sztouyun.advertisingsystem.viewmodel.adProfitShare.AdvertisementProfitStatisticInfo;
import com.sztouyun.advertisingsystem.viewmodel.adProfitShare.AdvertisementStoreProfitPeriodStatisticInfo;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.AdvertisementProfitShareDaysPageInfoRequest;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import org.apache.calcite.linq4j.Linq4j;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QSort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Service
public class AdvertisementProfitStatisticService extends BaseService {

    @Autowired
    AdvertisementProfitStatisticRepository advertisementProfitStatisticRepository;
    @Autowired
    StoreProfitStatisticRepository storeProfitStatisticRepository;
    @Autowired
    AdvertisementStoreProfitPeriodStatisticRepository advertisementStoreProfitPeriodStatisticRepository;
    @Autowired
    StoreInfoRepository storeInfoRepository;

    private final QAdvertisementProfitStatistic qAdvertisementProfitStatistic = QAdvertisementProfitStatistic.advertisementProfitStatistic;
    private final QAdvertisementStoreProfitPeriodStatistic qAdvertisementStoreProfitPeriodStatistic = QAdvertisementStoreProfitPeriodStatistic.advertisementStoreProfitPeriodStatistic;

    public Map<String,Double> getAdvertisementStoreProfitPeriodStatistic(List<String> advertisementIds, String storeId) {
        List<AdvertisementStoreProfitPeriodStatisticInfo> advertisementStoreProfitPeriodStatisticInfo = advertisementStoreProfitPeriodStatisticRepository.findAllAuthorized(q ->
                q.select(Projections.bean(AdvertisementStoreProfitPeriodStatisticInfo.class,
                        qAdvertisementStoreProfitPeriodStatistic.advertisementId.as("advertisementId"),
                        qAdvertisementStoreProfitPeriodStatistic.shareAmount.multiply(100).sum().divide(100).as("shareAmount")))
                        .from(qAdvertisementStoreProfitPeriodStatistic)
                        .where(qAdvertisementStoreProfitPeriodStatistic.advertisementId.in(advertisementIds).and(qAdvertisementStoreProfitPeriodStatistic.storeId.eq(storeId)))
                        .groupBy(qAdvertisementStoreProfitPeriodStatistic.advertisementId));
        return Linq4j.asEnumerable(advertisementStoreProfitPeriodStatisticInfo).toMap(a -> a.getAdvertisementId(), b -> b.getShareAmount());
    }

    public Page<AdvertisementProfitStatisticInfo> getAdvertisementStoreProfitPeriodStatisticInfo(AdvertisementProfitSharePageInfoRequest request) {
        if(!storeInfoRepository.exists(request.getStoreId()))
            throw new BusinessException("门店ID无效");
        Pageable pageable = new MyPageRequest(request.getPageIndex(), request.getPageSize());
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(qAdvertisementProfitStatistic.storeId.eq(request.getStoreId()));
        predicate.and(qAdvertisementProfitStatistic.advertisement.advertisementStatus.in(com.sztouyun.advertisingsystem.utils.StringUtils.stringToInts(request.getProfitShareStatus(),Constant.SEPARATOR)));
        if (!StringUtils.isEmpty(request.getAdvertisementName())) {
            predicate.and(qAdvertisementProfitStatistic.advertisement.advertisementName.contains(request.getAdvertisementName()));
        }
        if (request.getEnableProfitShare()!=null) {
            predicate.and(qAdvertisementProfitStatistic.advertisement.enableProfitShare.eq(request.getEnableProfitShare()));
        }
        if(request.getStartTime()!=null){
            predicate.and(qAdvertisementProfitStatistic.advertisement.effectiveStartTime.goe(request.getStartTime()));
        }
        if(request.getEndTime()!=null){
            predicate.and(qAdvertisementProfitStatistic.advertisement.effectiveStartTime.lt(new LocalDate(request.getEndTime()).plusDays(1).toDate()));
        }

        return advertisementProfitStatisticRepository.findAllAuthorized(q ->
                q.select(Projections.bean(AdvertisementProfitStatisticInfo.class,
                        qAdvertisementProfitStatistic.advertisementId.as("advertisementId"),
                        qAdvertisementProfitStatistic.advertisement.advertisementName.as("advertisementName"),
                        qAdvertisementProfitStatistic.advertisement.advertisementType.as("advertisementType"),
                        qAdvertisementProfitStatistic.advertisement.advertisementStatus.as("advertisementStatus"),
                        qAdvertisementProfitStatistic.advertisement.effectiveStartTime.as("effectiveStartTime"),
                        qAdvertisementProfitStatistic.advertisement.effectiveEndTime.as("effectiveEndTime"),
                        qAdvertisementProfitStatistic.advertisement.enableProfitShare.as("enableProfitShare"),
                        qAdvertisementProfitStatistic.advertisement.contract.ownerId.as("ownerId"),
                        qAdvertisementProfitStatistic.advertisement.contract.platform.as("terminalNames"),
                        qAdvertisementProfitStatistic.advertisement.contractId.as("contractId"),
                        qAdvertisementProfitStatistic.advertisement.creatorId.as("creatorId")))
                        .from(qAdvertisementProfitStatistic)
                        .innerJoin(qAdvertisementProfitStatistic.advertisement)
                        .innerJoin(qAdvertisementProfitStatistic.advertisement.contract)
                        .where(predicate)
                        .groupBy(qAdvertisementProfitStatistic.advertisementId).orderBy(qAdvertisementProfitStatistic.advertisement.effectiveStartTime.desc()), pageable, false);
    }

    public Double getShareAmountCount(AdvertisementProfitShareDaysPageInfoRequest request){
        return advertisementProfitStatisticRepository.findOne(q->q.select(qAdvertisementProfitStatistic.shareAmount.multiply(100).sum().divide(100)).from(qAdvertisementProfitStatistic).where(filter(request)));
    }

    public Page<AdvertisementProfitStatistic> getAdvertisementProfitDays(AdvertisementProfitShareDaysPageInfoRequest request){
        Pageable pageable = new MyPageRequest(request.getPageIndex(), request.getPageSize(), new QSort(qAdvertisementProfitStatistic.shareAmount.desc()));
        return advertisementProfitStatisticRepository.findAllAuthorized(filter(request), pageable, new JoinDescriptor().leftJoin(qAdvertisementProfitStatistic.contract).leftJoin(qAdvertisementProfitStatistic.advertisement));
    }

    private BooleanBuilder filter(AdvertisementProfitShareDaysPageInfoRequest request) {
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(qAdvertisementProfitStatistic.storeProfitStatisticId.eq(request.getStoreProfitStatisticId()));
        if (!StringUtils.isEmpty(request.getAdvertisementName())) {
            predicate = predicate.and(qAdvertisementProfitStatistic.advertisement.advertisementName.contains(request.getAdvertisementName()));
        }
        if (request.getActive()!=null){
            predicate = predicate.and(qAdvertisementProfitStatistic.actived.eq(request.getActive()));
        }
        if (request.getEnableProfitShare()!=null) {
            predicate = predicate.and(qAdvertisementProfitStatistic.enableProfitShare.eq(request.getEnableProfitShare()));
        }
        if (!StringUtils.isEmpty(request.getProfitShareStatus())) {
            predicate = predicate.and(qAdvertisementProfitStatistic.advertisement.advertisementStatus.in(com.sztouyun.advertisingsystem.utils.StringUtils.stringToInts(request.getProfitShareStatus(), Constant.SEPARATOR)));
        }
        return predicate;
    }

}
