package com.sztouyun.advertisingsystem.repository.adProfitShare;

import com.querydsl.core.BooleanBuilder;
import com.sztouyun.advertisingsystem.model.adProfitShare.QSettledStoreProfit;
import com.sztouyun.advertisingsystem.model.adProfitShare.SettledStatusEnum;
import com.sztouyun.advertisingsystem.model.adProfitShare.SettledStoreProfit;
import com.sztouyun.advertisingsystem.repository.BaseRepository;

/**
 * Created by wenfeng on 2018/1/15.
 */
public interface SettledStoreProfitRepository extends BaseRepository<SettledStoreProfit>{
    SettledStoreProfit findById(String id);

    void deleteById(String settledStoreProfitId);

    @Override
    default BooleanBuilder getAuthorizationFilter() {
        return new BooleanBuilder(QSettledStoreProfit.settledStoreProfit.settleStatus.ne(SettledStatusEnum.UnConformed.getValue()));
    }

}
