package com.sztouyun.advertisingsystem.repository.monitor;

import com.querydsl.core.BooleanBuilder;
import com.sztouyun.advertisingsystem.model.monitor.AdvertisementMonitorStatistic;
import com.sztouyun.advertisingsystem.model.monitor.QAdvertisementMonitorStatistic;
import com.sztouyun.advertisingsystem.repository.BaseRepository;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;

import java.util.List;


public interface AdvertisementMonitorStatisticRepository extends BaseRepository<AdvertisementMonitorStatistic> {

    @Override
    default BooleanBuilder getAuthorizationFilter() {
        return AuthenticationService.getUserAuthenticationFilter(QAdvertisementMonitorStatistic.advertisementMonitorStatistic.contract.ownerId);
    }
}
