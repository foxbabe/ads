package com.sztouyun.advertisingsystem.repository.advertisement;

import com.querydsl.core.BooleanBuilder;
import com.sztouyun.advertisingsystem.model.advertisement.Advertisement;
import com.sztouyun.advertisingsystem.model.advertisement.QAdvertisement;
import com.sztouyun.advertisingsystem.repository.BaseRepository;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;

public interface AdvertisementRepository extends BaseRepository<Advertisement> {
    boolean existsByAdvertisementName(String id);

    boolean existsByIdNotAndAdvertisementName(String id,String advertisementName);

    @Override
    default BooleanBuilder getAuthorizationFilter() {
        return AuthenticationService.getUserAuthenticationFilter(QAdvertisement.advertisement.contract.ownerId);
    }

    boolean existsByCreatorId(String userId);
}
