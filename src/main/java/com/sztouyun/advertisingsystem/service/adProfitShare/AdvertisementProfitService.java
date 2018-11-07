package com.sztouyun.advertisingsystem.service.adProfitShare;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.model.adProfitShare.AdvertisementProfit;
import com.sztouyun.advertisingsystem.model.adProfitShare.QAdvertisementProfit;
import com.sztouyun.advertisingsystem.model.advertisement.QAdvertisement;
import com.sztouyun.advertisingsystem.model.contract.QContract;
import com.sztouyun.advertisingsystem.repository.adProfitShare.AdvertisementProfitRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.viewmodel.adProfitShare.AdvertisementProfitListRequest;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.querydsl.QSort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class AdvertisementProfitService extends BaseService {

    @Autowired
    AdvertisementProfitRepository advertisementProfitRepository;

    private final QAdvertisementProfit qAdvertisementProfit = QAdvertisementProfit.advertisementProfit;
    private final QContract qContract=QContract.contract;
    private final QAdvertisement qAdvertisement=QAdvertisement.advertisement;

    public AdvertisementProfit sumAdvertisementProfitStatistic() {
        return advertisementProfitRepository.findOneAuthorized(queryFactory -> queryFactory.select(Projections.bean(AdvertisementProfit.class,
            qAdvertisementProfit.shareAmount.multiply(100).sum().divide(100).as("shareAmount"),
            qAdvertisementProfit.settledAmount.multiply(100).sum().divide(100).as("settledAmount"),
            qAdvertisementProfit.unsettledAmount.multiply(100).sum().divide(100).as("unsettledAmount")
            )).from(qAdvertisementProfit));
    }

    public List<AdvertisementProfit> getAdvertisementProfitRank() {
        return advertisementProfitRepository.findAllAuthorized(queryFactory -> queryFactory.selectFrom(qAdvertisementProfit
                ).where(qAdvertisementProfit.shareAmount.gt(0)).limit(10).orderBy(qAdvertisementProfit.shareAmount.desc())
        );
    }

    public Page<AdvertisementProfit> getAdvertisementProfitList(AdvertisementProfitListRequest request) {
        MyPageRequest pageRequest=new MyPageRequest(request.getPageIndex(),request.getPageSize(),new QSort(qAdvertisement.effectiveStartTime.desc()));
        BooleanBuilder predicate = filter(request);
        return advertisementProfitRepository.findAllAuthorized(q->
                q.selectFrom(qAdvertisementProfit).innerJoin(qAdvertisementProfit.advertisement,qAdvertisement).fetchJoin()
                .innerJoin(qAdvertisement.contract,qContract).fetchJoin()
                .innerJoin(qContract.owner)
                .where(predicate).orderBy(qAdvertisement.effectiveStartTime.desc())
                ,pageRequest);
    }

    private BooleanBuilder filter(AdvertisementProfitListRequest request) {
        BooleanBuilder predicate =new BooleanBuilder();
        predicate.and(qAdvertisement.advertisementStatus.in(com.sztouyun.advertisingsystem.utils.StringUtils.stringToInts(request.getAdvertisementStatus(), Constant.SEPARATOR)));
        if(!StringUtils.isEmpty(request.getAdvertisementName())){
            predicate.and(qAdvertisement.advertisementName.contains(request.getAdvertisementName()));
        }
        if(!StringUtils.isEmpty(request.getNickname())){
            predicate.and(qContract.owner.nickname.contains(request.getNickname()));
        }
        if(request.getEnableProfitShare()!=null){
            predicate.and(qAdvertisement.enableProfitShare.eq(request.getEnableProfitShare()));
        }
        if(request.getEffectiveStartTime()!=null){
            predicate.and(qAdvertisement.effectiveStartTime.goe(request.getEffectiveStartTime()));
        }
        if(request.getEffectiveEndTime()!=null){
            predicate.and(qAdvertisement.effectiveStartTime.loe(request.getEffectiveEndTime()));
        }
        return predicate;
    }

    public Double getTotalAmount(AdvertisementProfitListRequest request){
        return advertisementProfitRepository.findOneAuthorized(q->q.select(qAdvertisementProfit.shareAmount.multiply(100).sum().divide(100)).from(qAdvertisementProfit)
                .innerJoin(qAdvertisementProfit.advertisement,qAdvertisement)
                .innerJoin(qAdvertisement.contract,qContract)
                .innerJoin(qContract.owner).where(filter(request)));
    }
}
