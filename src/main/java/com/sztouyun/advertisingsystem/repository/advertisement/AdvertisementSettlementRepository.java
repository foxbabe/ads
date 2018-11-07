package com.sztouyun.advertisingsystem.repository.advertisement;

import com.querydsl.core.BooleanBuilder;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementSettlement;
import com.sztouyun.advertisingsystem.model.advertisement.QAdvertisementSettlement;
import com.sztouyun.advertisingsystem.repository.BaseRepository;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;

public interface AdvertisementSettlementRepository extends BaseRepository<AdvertisementSettlement> {
    @Override
    default BooleanBuilder getAuthorizationFilter() {
        return AuthenticationService.getUserAuthenticationFilter(QAdvertisementSettlement.advertisementSettlement.advertisement.contract.ownerId);
    }

    void deleteByAdvertisementId(String advertisementId);
}
