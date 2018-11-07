package com.sztouyun.advertisingsystem.service.adProfitShare;

import com.querydsl.core.BooleanBuilder;
import com.sztouyun.advertisingsystem.common.jpa.JoinDescriptor;
import com.sztouyun.advertisingsystem.model.adProfitShare.QStoreProfitStatistic;
import com.sztouyun.advertisingsystem.model.adProfitShare.StoreProfitStatistic;
import com.sztouyun.advertisingsystem.repository.adProfitShare.StoreProfitStatisticRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.viewmodel.adProfitShare.StoreProfitStreamRequest;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QSort;
import org.springframework.stereotype.Service;

/**
 * 收益流水
 * @author guangpu.yan
 * @create 2018-01-11 9:46
 **/
@Service
public class StoreProfitStreamService extends BaseService {
    private final QStoreProfitStatistic qStoreProfitStatistic = QStoreProfitStatistic.storeProfitStatistic;

    @Autowired
    StoreProfitStatisticRepository storeProfitStatisticRepository;

    /**
     * 获取收益流水列表
     * @param queryRequest
     * @return
     */
    public Page<StoreProfitStatistic> getStoreProfitStream(StoreProfitStreamRequest queryRequest) {
        Pageable pageable = new MyPageRequest(queryRequest.getPageIndex(), queryRequest.getPageSize(),new QSort(qStoreProfitStatistic.profitDate.desc()));
        return storeProfitStatisticRepository.findAll(filter(queryRequest), pageable,new JoinDescriptor().leftJoin(qStoreProfitStatistic.storeProfitStatisticExtension));
    }

    /**
     * 收益总和
     * @param queryRequest
     * @return
     */
    public Double getShareAmountCount(StoreProfitStreamRequest queryRequest){
        return storeProfitStatisticRepository.findOne(q->q.select(qStoreProfitStatistic.shareAmount.multiply(100).sum().divide(100)).from(qStoreProfitStatistic).where(filter(queryRequest)));
    }

    /**
     * 获取收益流水明细
     * @param id
     * @return
     */
    public StoreProfitStatistic getStoreProfitStreamDetail(String id) {
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(qStoreProfitStatistic.id.eq(id));
        return storeProfitStatisticRepository.findOne(predicate,new JoinDescriptor().leftJoin(qStoreProfitStatistic.storeProfitStatisticExtension).leftJoin(qStoreProfitStatistic.storeInfo));
    }

    private BooleanBuilder filter(StoreProfitStreamRequest queryRequest){
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(qStoreProfitStatistic.storeId.eq(queryRequest.getStoreId()));

        if (queryRequest.getOpeningTimeStandardIs()!=null) {
            predicate.and(qStoreProfitStatistic.openingTimeStandardIs.eq(queryRequest.getOpeningTimeStandardIs()));
        }
        if (queryRequest.getOrderStandardIs()!=null) {
            predicate.and(qStoreProfitStatistic.orderStandardIs.eq(queryRequest.getOrderStandardIs()));
        }

        if (queryRequest.getBeginTime()!=null) {
            predicate.and(qStoreProfitStatistic.profitDate.goe(queryRequest.getBeginTime()));
        }

        if (queryRequest.getEndTime()!=null) {
            predicate.and(qStoreProfitStatistic.profitDate.loe(queryRequest.getEndTime()));
        }

        if (queryRequest.getSettled()!=null) {
            predicate.and(qStoreProfitStatistic.settled.eq(queryRequest.getSettled()));
        }
        return predicate;
    }
}
