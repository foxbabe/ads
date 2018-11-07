package com.sztouyun.advertisingsystem.repository.partner.advertisement;


import com.querydsl.core.BooleanBuilder;
import com.sztouyun.advertisingsystem.model.partner.advertisement.PartnerAdvertisement;
import com.sztouyun.advertisingsystem.model.partner.advertisement.QPartnerAdvertisement;
import com.sztouyun.advertisingsystem.repository.BaseRepository;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;

public interface PartnerAdvertisementRepository extends BaseRepository<PartnerAdvertisement> {

    @Override
    default BooleanBuilder getAuthorizationFilter(){
        return AuthenticationService.getUserAuthenticationFilter(QPartnerAdvertisement.partnerAdvertisement.partner.ownerId);
    }
}
