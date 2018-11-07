package com.sztouyun.advertisingsystem.repository.adProfitShare;

import com.querydsl.core.BooleanBuilder;
import com.sztouyun.advertisingsystem.model.adProfitShare.AdvertisementProfit;
import com.sztouyun.advertisingsystem.model.adProfitShare.QAdvertisementProfit;
import com.sztouyun.advertisingsystem.repository.BaseRepository;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;

public interface AdvertisementProfitRepository extends BaseRepository<AdvertisementProfit> {

    @Override
    default BooleanBuilder getAuthorizationFilter() {
        return AuthenticationService.getUserAuthenticationFilter(QAdvertisementProfit.advertisementProfit.advertisement.contract.ownerId);
    }
}
