package com.sztouyun.advertisingsystem.repository.monitor;

import com.querydsl.core.BooleanBuilder;
import com.sztouyun.advertisingsystem.model.account.QUser;
import com.sztouyun.advertisingsystem.model.monitor.PartnerAdvertisementMonitorStatistic;
import com.sztouyun.advertisingsystem.model.monitor.QPartnerAdvertisementMonitorStatistic;
import com.sztouyun.advertisingsystem.repository.BaseRepository;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;


public interface PartnerAdvertisementMonitorStatisticRepository extends BaseRepository<PartnerAdvertisementMonitorStatistic> {
    @Override
    default BooleanBuilder getAuthorizationFilter() {
        return AuthenticationService.getUserAuthenticationFilter(QPartnerAdvertisementMonitorStatistic.partnerAdvertisementMonitorStatistic.partner.owner.id);
    }
}
