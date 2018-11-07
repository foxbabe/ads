package com.sztouyun.advertisingsystem.mapper;

import com.sztouyun.advertisingsystem.viewmodel.adProfitShare.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdvertisementProfitStatisticMapper {

    void supplementAdvertisementProfitStatistic(SupplementaryProfitShareRequest request);

    void updateAdvertisementActive(AdvertisementActiveUpdateRequest request);

    void updateAdvertisementProfit(AdvertisementProfitSettledInfo advertisementProfitSettledInfo);

    void updateStoreAdvertisementProfit(AdvertisementProfitSettledInfo advertisementProfitSettledInfo);

    void updateAdvertisementProfitShareAmount(PeriodStoreProfitShareCalculateViewModel viewModel);

    void updateAdvertisementStoreProfitShareAmount(PeriodStoreProfitShareCalculateViewModel viewModel);

    List<SettledDetailAdvertisementStatisticViewModel> getSettledDetailAdvertisementStatistic(String settledStoreProfitId);

    void supplementAdvertisementProfitQualifiedInfo(SupplementProfitInfo info);
}
