package com.sztouyun.advertisingsystem.mapper;

import com.sztouyun.advertisingsystem.viewmodel.adProfitShare.AdvertisementPeriodStoreProfitStatisticRequest;
import com.sztouyun.advertisingsystem.viewmodel.adProfitShare.PeriodStoreProfitShareCalculateViewModel;
import com.sztouyun.advertisingsystem.viewmodel.store.ContractStoreQueryRequest;
import com.sztouyun.advertisingsystem.viewmodel.store.PeriodStoreProfitStatisticInfo;
import com.sztouyun.advertisingsystem.viewmodel.store.PeriodStoreProfitStatisticViewModel;
import com.sztouyun.advertisingsystem.viewmodel.store.StoreInfoMonthList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PeriodStoreProfitStatisticMapper {
    void addPeriodStoreProfitShareStatistic(PeriodStoreProfitShareCalculateViewModel request);

    void deletePeriodStoreProfitShareStatistic(List<String> periodStoreProfitShareStatisticIds);

    List<PeriodStoreProfitStatisticInfo> getAdvertisementStoreProfitPeriodStatistic(PeriodStoreProfitStatisticViewModel periodStoreProfitStatisticViewModel);

    List<StoreInfoMonthList> getStoreMonthProfitList(AdvertisementPeriodStoreProfitStatisticRequest request);

    List<StoreInfoMonthList> getAdvertisementStoreMonthProfitList(ContractStoreQueryRequest request);

}